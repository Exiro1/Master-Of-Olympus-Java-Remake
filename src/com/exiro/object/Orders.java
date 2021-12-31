package com.exiro.object;

public enum Orders {ACCEPT, EMPTY, OBTAIN, REJECT;



    public Orders next() {
        if(this == ACCEPT){
            return OBTAIN;
        }else if(this == OBTAIN){
            return EMPTY;
        }else if(this == EMPTY){
            return REJECT;
        }else if(this == REJECT){
            return ACCEPT;
        }
        return ACCEPT;
    }
}
