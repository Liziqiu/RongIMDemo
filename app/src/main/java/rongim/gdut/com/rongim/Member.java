package rongim.gdut.com.rongim;

import android.graphics.drawable.Drawable;

/**
 * Created by Zhiqiang on 2015/11/3.
 */
public class Member {
    public Drawable icon;
    public String name;
    public int type;

    public Member(Drawable icon, String name, int type) {
        this.icon = icon;
        this.name = name;
        this.type = type;
    }
}
