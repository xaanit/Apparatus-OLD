package me.xaanit.apparatus;

import com.google.gson.Gson;
import me.xaanit.apparatus.objects.json.Guild;

import java.util.Arrays;
import java.util.Random;

/**
 * Created by Jacob on 5/14/2017.
 */
public class Testing {

    public static void main(String[] args) {
        System.out.println(new Gson().toJson(new Guild()));
        Random random = new Random();
        int length = random.nextInt(6);
        int[] arr = new int[length];
        for(int i = 0; i < length; i++)
            arr[i] = random.nextInt(20);
        System.out.println(Arrays.toString(arr));
        for (int i = 0; i < arr.length; i++) {
            arr = rotateRight(arr);
            System.out.println(Arrays.toString(arr));
        }
    }


    public static int[] rotateRight(int[] arr) {
        if (arr.length <= 1)
            return arr;
        int[] res = new int[arr.length];
        res[0] = arr[arr.length - 1];
        for (int i = 0; i < arr.length - 1; i++)
            res[i + 1] = arr[i];
        return res;
    }
}
