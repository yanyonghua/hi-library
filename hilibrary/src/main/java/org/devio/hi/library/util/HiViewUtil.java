package org.devio.hi.library.util;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * @Author yanyonghua
 * @Date 2022/10/17-11:03
 * @Des $.
 */
public class HiViewUtil {
    /**
     * 获取指定类型的子View
     *
     * @param group viewGroup
     * @param cls   如：RecyclerView.class
     * @param <T>
     * @return 指定类型的View
     */
    public static <T> T findTypeView(@Nullable ViewGroup group, Class<T> cls) {
        if (group == null) {
            return null;
        }
        Deque<View> deque = new ArrayDeque<>();
        deque.add(group);
        while (!deque.isEmpty()) {
            //拿出第一个view
            View node = deque.removeFirst();
            //判断释放时当前需要找的类
            if (cls.isInstance(node)) {
                //通过cast方法返回
                return cls.cast(node);
            } else if (node instanceof ViewGroup) {
                ViewGroup container = (ViewGroup) node;
                //再挨个放入队列中取
                for (int i = 0, count = container.getChildCount(); i < count; i++) {
                    deque.add(container.getChildAt(i));
                }
            }
        }
        return null;
    }

    public static boolean isActivityDestoryed(Context context){
      Activity activity = findActivity(context);
      if (activity!=null){
          return activity.isDestroyed()||activity.isFinishing();
      }
      return true;
    }

    private static Activity findActivity(Context context) {
        //怎么判断context是不是activity 类型的
        if (context instanceof Activity) {
            return (Activity) context;
        } else if (context instanceof ContextWrapper){
            return findActivity(((ContextWrapper) context).getBaseContext());
        }
        return null;
    }
}
