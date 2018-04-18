package fr.antoinebaudot.lab1mad;

import android.support.design.widget.NavigationView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Antoine on 18/04/2018.
 */

public class NavigationDrawerItem {

    private String title ;
    private int imageId ;

    public String getTitle() {
        return title;
    }

    public int getImageId() {
        return imageId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public static List<NavigationDrawerItem> getData(){
        List<NavigationDrawerItem> dataList = new ArrayList<>();

        int[] imageIds = getImages();
        String[] titles = getTitles();

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
                R.drawable.ic_action_edit,
                R.drawable.ic_photo_camera_black_24dp
        };
    }

    private static String[] getTitles() {
        return new String[] {
                "item1","item2","item3"
        };
    }
}
