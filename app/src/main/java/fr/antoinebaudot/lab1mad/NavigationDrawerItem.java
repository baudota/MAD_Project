package fr.antoinebaudot.lab1mad;

import android.support.design.widget.NavigationView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Antoine on 18/04/2018.
 */

public class NavigationDrawerItem {

    private int title ;
    private int imageId ;

    public int getTitle() {
        return title;
    }

    public int getImageId() {
        return imageId;
    }

    public void setTitle(int title) {
        this.title = title;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public static List<NavigationDrawerItem> getData(){
        List<NavigationDrawerItem> dataList = new ArrayList<>();

        int[] imageIds = getImages();
        int[] titles = getTitles();

        for (int i =0 ; i < titles.length; i++){
            NavigationDrawerItem navItem = new NavigationDrawerItem();
            navItem.setTitle(titles[i]);
            navItem.setImageId(imageIds[i]);
            dataList.add(navItem);
        }
        return dataList;
    }


    private static int[] getImages(){
        return new int[]{
                R.drawable.ic_person_black_24dp,
                R.drawable.icon_book,
                R.drawable.ic_searchbooks,
                R.drawable.ic_requests,
                R.drawable.icon_disconnect

        };
    }

    private static int[] getTitles() {

        return new int[] {
                R.string.menuProfile,
                R.string.menuUserBooks,
                R.string.searchbooks,
                R.string.requests,
                R.string.menuLeave

        };
    }
}
