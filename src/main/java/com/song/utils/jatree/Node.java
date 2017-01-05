package com.song.utils.jatree;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by Song on 2017/1/4.
 * 树节点
 */
public class Node implements Serializable{
    public Object element;
    public Map<Object,Node> child;
}
