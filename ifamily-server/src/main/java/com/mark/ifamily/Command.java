package com.mark.ifamily;

/**
 * Created by mark.zhu on 2016/9/29.
 */
public interface Command {
    void execute(Options options) throws Exception;
}
