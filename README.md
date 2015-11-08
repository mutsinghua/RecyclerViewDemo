
RecyclerView是google在去年的IO大会新推出的用以取代ListView的新控件，它的扩展性与灵活性都显著优于ListView。

##RecyclerView的优点
其优点，我总结如下，

 - 省内存。多个RecyclerView可共用View。这样，如果一个应用中有多个类似的界面，比如应用市场各榜单的界面是差不多的，这样可以省非常多的内存。
 - 布局灵活。RecycleView的布局方式与其本身彻底分离了，采用了设计模式中的策略模式，交由LayoutManager来实现，RecycleView只关心View的使用与回收，不管是实现传统的ListView，还是GridView, 甚至是瀑布流，修改就是分分钟的事。
 - 固化了ViewHolder的使用。ViewHolder作为ListView的推荐优化方式，早已深入人心。现在，在RecyclerView中，已作为机制固定下来。
 - 优化了更新通知，极大的提高了性能。ListView在数据改变时只能使用notifyDataSetChanged方法，而在RecyclerView中，可以在视图刷新上采用更加细腻的方法。

```
notifyItemChanged(int), 
notifyItemInserted(int), 
notifyItemRemoved(int), 
notifyItemRangeChanged(int, int), 
notifyItemRangeInserted(int, int), 
notifyItemRangeRemoved(int, int)
```
删除，增加都有单独通知，甚至也可以单独刷新某一个视图，这个提供了极大的方便 。

 - 方便的动画支持。RecyclerView提供了ItemAnimator，可以方便在增加或删除Item的时候进行播放，提高用户体验。

当然，万事万物都有两面性，其缺点是缺少内置点击事件。RecyclerView没有ListView的onItemClickListener或是onItemLongClickListener，只能使用onItemTouchListener事件，也就是接受Touch事件，自已写逻辑来实现click.

##从ListView到RecyclerView
下面就接着来说它的使用方法，我们从最简单的入手，即实现ListView的功能。
###RecyclerView的实现步骤

 - 把RecyclerView引入到项目中，如果是使用gradle，在dependeceny中加入：`compile 'com.android.support:recyclerview-v7:23.+'`
 - 定义ViewHolder，即相当于每个ListItem。
 - 定义Adapter,   即相当于ListView的Adapter.
 - 为RecyclerView设置一个布局管理器LayoutManager.

在开始我们先介绍几个重点的类。

 - RecyclerView.ViewHolder，相当于View的缓存，用于提高数据绑定时的性能
 - RecyclerView.Adapter，用于设置数据源，比如数据个数等
 - RecyclerView.LayoutManager，用于设置RecyclerView的排版方式，如网格布局，错位布局等。

除了最后一个有点陌生之外，其他都很熟悉。

####RecyclerView.ViewHolder的实现
RecyclerView.ViewHolder的一个简单实现

```
/**
 * Created by REXZOU on 11/2/2015.
 */
public class MyViewHolder extends RecyclerView.ViewHolder{

    public TextView mTextView;

    public MyViewHolder(View itemView) {
        super(itemView);

        mTextView = (TextView) itemView.findViewById(android.R.id.text1);
    }
}
```
默认有一个带有唯一参数的构造，itemView，就是ViewHolder所容纳的View，任何时候都可以去访问。当然，通常为了方便，我们需要在ViewHolder中定义常用的视图，比如mTextView，在构造函数进行初始化。这里的成员一般都是public,以便访问。

####RecyclerView.Adapter的实现

Adapter需要实现三个方法，
adapter的实现
onCreateViewHolder(ViewGroup parent, int viewType) 
onBindViewHolder(MyHoldView holder, int position)
getItemCount()

onCreateViewHolder这个方法需要返回之前创建的ViewHolder,  现在比较方便的是自带Context，不需要再从外面传过来。当然，RecyclerView的优势又显露出来了，不再需要我们去做Item复用，RecyclerView在内部就帮我们实现了（有兴趣的同学可以参考源码getViewForPosition这个方法）

onBindViewHolder主要是用于数据与View绑定。

getItemCount获取item的个数
```

/**
 * Created by Rex on 2015/11/3.
 */
public class MyAdapter extends RecyclerView.Adapter<MyHoldView> {

    private List<PackageInfo> packageInfos;

    public MyAdapter(List<PackageInfo> packageInfos) {
        this.packageInfos = packageInfos;
    }

    @Override
    public MyHoldView onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return new MyHoldView(layoutInflater.inflate(android.R.layout.simple_list_item_1,null));
    }

    @Override
    public void onBindViewHolder(MyHoldView holder, int position) {
        PackageInfo packageInfo = packageInfos.get(position);

        holder.textView.setText(packageInfo.packageName);
    }

    @Override
    public int getItemCount() {
        return packageInfos.size();
    }
}
```

####设置LayoutManager
LayoutManager是新概念。这是用于设置RecyclerView的显示方式，也可以说是RecyclerView的精华部份，是像ListView还是GridView并且能轻松切换。妈妈再也不怕产品改需求了。
#####LinearLayoutManager
Android为我们提供了3种布局，最简单的是LinearLayoutManager，使用方法是传一个Context即可。
效果如下：
![这里写图片描述](http://img.blog.csdn.net/20151106002025885)

#####GridLayoutManager
GridLayoutManager稍复杂一点，可以设置方向与列数。

```
public GridLayoutManager(Context context, int spanCount)
public GridLayoutManager(Context context, int spanCount, int orientation,
            boolean reverseLayout)
```
比如设置

```
recyclerView.setLayoutManager(new GridLayoutManager(this,2));
```
效果是这样的，变成了纵向滚动的格子
![这里写图片描述](http://img.blog.csdn.net/20151106002520661)

如果这样设置，

```
recyclerView.setLayoutManager(new GridLayoutManager(this,2, GridLayoutManager.HORIZONTAL,false));
```
效果就成了横向滚动
![这里写图片描述](http://img.blog.csdn.net/20151106002750849)

######StaggeredGridLayoutManager
这是在必须时可以交错显示，类似于前段时间 非常火的瀑布流。

```
recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
```
效果如下，现在看起来和上面变化不大
![这里写图片描述](http://img.blog.csdn.net/20151106003354765)

我们把布局稍微改下，可以看到瀑布流效果。

```
<?xml version="1.0" encoding="utf-8"?>
<android.support.v7.widget.CardView xmlns:android="http://schemas.android.com/apk/res/android"
    android:orientation="vertical" android:layout_width="wrap_content"
    android:layout_margin="10dp"
    android:layout_height="wrap_content">

    <TextView
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:id="@+id/text1"
        android:textSize="18dp"
        />
</android.support.v7.widget.CardView>
```
![这里写图片描述](http://img.blog.csdn.net/20151106004213828)


###轻量化的通知
前面提到RecyclerView的Adapter对数据变动的通知作了优化，更加的精准与轻量。现在我们就来介绍怎么使用。

Adapter的通知分为单体通知和群体通知。在底层的实现中，实际上是最终调用的群体通知。

```
public final void notifyItemInserted(int position) {
            mObservable.notifyItemRangeInserted(position, 1);
        }
public final void notifyItemMoved(int fromPosition, int toPosition) {
            mObservable.notifyItemMoved(fromPosition, toPosition);
        }
public final void notifyItemRangeInserted(int positionStart, int itemCount) {
            mObservable.notifyItemRangeInserted(positionStart, itemCount);
        }
```

现在我们就来一个个的进行操作。
比如增加一项数据：

```
packageInfoList.add(0, packageInfo);
 adapter.notifyItemInserted(0);
 recyclerView.scrollToPosition(0); \\可选，主要作用是让recyclerview滚动至新增加的一行
```

删除一项数据

```
packageInfoList.remove(0);
adapter.notifyItemRemoved(0);
```

更新一项数据，

```
adapter.notifyItemChanged(0);
```

对于批量的修改也很简单，只要在以上notify系列的第二个参数加上批量的个数即可。
adapter.notifyItemInserted(0, 100); //从0开始，插入100个数据。


###ItemAnimator
由于Adapter的通知非常精准，因此，在对数据删除增加时出动画是非常方便的。只要继承RecyclerView.ItemAnimator这个类。

> PS：有点麻烦的是这个类在23.1的版本作了修改，与之前的版本不兼容，Google为了解决这个问题，提供了一个android.support.v7.widget.SimpleItemAnimator来向下兼容，当你的项目升级到23.1而报错的话，只需要把你的基类RecyclerView.ItemAnimator改为SimpleItemAnimator即可。

当然，实现这个类还是比较复杂的，要实现多个方法，指定在不同场景，如添加，删除等动画效果，幸运的是有大牛已经为我们写好一些实现方式，可以满足大多数的需要，使用时只要引用这个库就好。

```
compile 'jp.wasabeef:recyclerview-animators:2.0.0'
```
对于23.1以下的编译环境，要使用。原因就是上面我说的不兼容问题导致的。

```
compile 'jp.wasabeef:recyclerview-animators:1.3.0'
```

引入这个库后，我们就可以使用代码：

```
recyclerView.setItemAnimator(new SlideInLeftAnimator());
recyclerView.getItemAnimator().setAddDuration(500);
recyclerView.getItemAnimator().setRemoveDuration(500);
recyclerView.getItemAnimator().setMoveDuration(500);
recyclerView.getItemAnimator().setChangeDuration(500);
```

###ItemDecoration
ListView中有个属性叫divider，用于定义Item之间的分隔线。RecyclerView把这个divider的概念进行了扩展，改为ItemDecoration。这样，我们可以实现的效果就更加灵活，不必从上到下都使用统一的divider效果，完全可以根据不同的item定制不同的特殊效果。当然，灵活带了的问题就是实现上会比之前要复杂一点。

这里用一个例子来讲它的一个实现。


```
/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class DividerItemDecoration extends RecyclerView.ItemDecoration {

    private static final int[] ATTRS = new int[]{
            android.R.attr.listDivider
    };

    public static final int HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL;

    public static final int VERTICAL_LIST = LinearLayoutManager.VERTICAL;

    private Drawable mDivider;

    private int mOrientation;

    public DividerItemDecoration(Context context, int orientation) {
        final TypedArray a = context.obtainStyledAttributes(ATTRS);
        mDivider = a.getDrawable(0);
        a.recycle();
        setOrientation(orientation);
    }

    public void setOrientation(int orientation) {
        if (orientation != HORIZONTAL_LIST && orientation != VERTICAL_LIST) {
            throw new IllegalArgumentException("invalid orientation");
        }
        mOrientation = orientation;
    }
	//这个是重点方法，用于真正绘制所需要的装饰效果，这里分为水平列表和垂直列表，通常情况我们都使用垂直列表，所以这里以垂直列表为例
    @Override
    public void onDraw(Canvas c, RecyclerView parent,RecyclerView.State state) {
        if (mOrientation == VERTICAL_LIST) {
            drawVertical(c, parent);
        } else {
            drawHorizontal(c, parent);
        }
    }

    public void drawVertical(Canvas c, RecyclerView parent) {
	    //首先，我们获取parent的padding，我们的画线不能画到padding外面去吧。
        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();

        final int childCount = parent.getChildCount();
        //这里，对每一个ChildView都要进行计算和绘制，如果有些ChildView有不一样的地方，可以在这里进行处理。
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int top = child.getBottom() + params.bottomMargin;
            final int bottom = top + mDivider.getIntrinsicHeight();
            //确定divider的位置，这里是画在每一个childView的下面，当然，你可以画在上面画，根据你的需要。
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    public void drawHorizontal(Canvas c, RecyclerView parent) {
        final int top = parent.getPaddingTop();
        final int bottom = parent.getHeight() - parent.getPaddingBottom();

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int left = child.getRight() + params.rightMargin;
            final int right = left + mDivider.getIntrinsicHeight();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

	//这个类主要是返回两个View之间的间距，返回值设置在outRect中，如果你的divider不占空间，则需要把outRect的大小设置为0。
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (mOrientation == VERTICAL_LIST) {
            outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());
        } else {
            outRect.set(0, 0, mDivider.getIntrinsicWidth(), 0);
        }
    }
}
```

###遗留问题
由于ItemDecoration不是一个View，因此在动画的时候，不能随着ItemAnimator进行。这样整个效果看起来就比较奇怪。我设想的一种解决方法是不使用这个ItemDecoration机制，把相关的效果直接写在ViewHolder中，不知大家有没有更好的办法。

示例代码：https://github.com/mutsinghua/RecyclerViewDemo.git




