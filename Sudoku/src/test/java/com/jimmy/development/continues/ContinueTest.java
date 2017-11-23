package com.jimmy.development.continues;

import org.junit.Test;

/**
 * Created by jinguochong on 23/11/2017.
 */

public class ContinueTest {

    @Test
    public void main(){
        for (int i = 0; i < 10; i++){
            if (i == 2)
                continue;
            System.out.println(i);
        }
    }
}
