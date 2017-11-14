package com.zhaoqi.psp.upload;

import com.zhaoqi.psp.domain.Part;

/**
 * Created by qi on 17-11-14.
 */
public interface HttpUploader {

    /**
     * Upload a file part.
     * @param part
     */
    public void upload(Part part);

    /**
     * NOtify server all uploading is done.
     * @param fileName
     * @param partCount
     */
    public void done(String fileName, long partCount);

}
