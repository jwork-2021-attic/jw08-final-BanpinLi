package com.umi.frame;

import com.umi.entity.bean.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class LoadMap {
    private int[][] midThings;
    private int[][] groundThings;

    public void load() throws Exception {
        loadMidThings();
        loadGroundThings();
    }

    private void loadMidThings() throws Exception {
        List<int[]> midList = getDataList("Server/", "src/main/resources/", "map/map-mid.txt");
        midThings = new int[midList.size()][midList.get(0).length];
        for(int i = 0;i < midThings.length;i++) {
            midThings[i] = midList.get(i);
        }
    }

    private void loadGroundThings() throws Exception {
        List<int[]> groundList = getDataList("Server/", "src/main/resources/", "map/map-ground.txt");
        groundThings = new int[groundList.size()][groundList.get(0).length];
        for(int i = 0; i < this.groundThings.length; i++) {
            this.groundThings[i] = groundList.get(i);
        }
    }

    // 加载地图组成的list数组，不同的目录下都进行查找一下，都找不到才抛出异常
    private List<int[]> getDataList(String projectName, String dir, String filename) throws Exception {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(dir + filename));
        } catch (Exception e) {
            try {
                br = new BufferedReader(new FileReader(filename));
            } catch (Exception ee) {
                try {
                    br = new BufferedReader(new FileReader(projectName + dir + filename));
                } catch (Exception eee) {
                    e.printStackTrace();
                    ee.printStackTrace();
                    throw eee;
                }
            }
        }

        List<int[]> thingList = new ArrayList<>();
        String dataString = br.readLine();
        while(dataString != null) {
            String[] data = dataString.split("\t");
            int[] cols = new int[data.length];
            for(int i = 0;i < data.length;i++) {
                cols[i] = Integer.parseInt(data[i]);
            }
            thingList.add(cols);
            dataString = br.readLine();
        }

        return thingList;
    }

    public void putInWorld(World world) throws Exception {
        putInWorldMid(world);
        putInWorldGround(world);
    }

    private void putInWorldMid(World world) throws Exception {
        for(int i = 0;i < midThings.length;i++) {
            for(int j = 0;j < midThings[0].length;j++) {
                if(midThings[i][j] == 0) {
                    world.putInMid(new Tree(world), i, j);
                } else if(midThings[i][j] == 1) {
                    world.putInMid(new Stone(world), i, j);
                } else if(midThings[i][j] == 5) {
                    world.putInMid(new Air(world), i, j);
                }
            }
        }
    }

    private void putInWorldGround(World world) throws Exception {
        for(int i = 0;i < groundThings.length;i++) {
            for(int j = 0;j < groundThings[0].length;j++) {
                if(groundThings[i][j] == 2) {
                    world.putInGround(new Turf(world), i, j);
                } else if(groundThings[i][j] == 3) {
                    world.putInGround(new Water(world), i, j);
                } else if(groundThings[i][j] == 4) {
                    world.putInGround(new Sand(world), i, j);
                }
            }
        }
    }
}
