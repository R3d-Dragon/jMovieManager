/**
 * Copyright (c) 2010-2015 Bryan Beck.
 * All rights reserved.
 * 
 * This project is licensed under LGPL v2.1.
 * See jMovieManager-license.txt for details.
 * 
 */
package jmm.utils;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.jboss.logging.Logger;

/**
 * Fast and memory efficent alternative to image.getScaledInstance()
 * 
 * @author Bryan Beck
 * @since 23.02.2013
 */
public class ImageScaler {
    /** Logger. */
    private static final Logger LOG = LoggerFactory.logger(ImageScaler.class);

    BufferedImage scaleImage(BufferedImage img, Dimension d) throws OutOfMemoryError {
        img = scaleByHalf(img, d);
        img = scaleExact(img, d);
        return img;
    }

    private BufferedImage scaleByHalf(BufferedImage img, Dimension d) throws OutOfMemoryError{
        int w = img.getWidth();
        int h = img.getHeight();
        float factor = getBinFactor(w, h, d);

        // make new size
        w *= factor;
        h *= factor;
        BufferedImage scaled = new BufferedImage(w, h,
                BufferedImage.TYPE_INT_RGB);
        Graphics2D g = scaled.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        g.drawImage(img, 0, 0, w, h, null);
        g.dispose();
        img.flush();
        return scaled;
    }

    private BufferedImage scaleExact(BufferedImage img, Dimension d) throws OutOfMemoryError{
//        float factor = getFactor(img.getWidth(), img.getHeight(), d);
        float factorX = getFactorX(img.getWidth(), d);
        float factorY = getFactorY(img.getHeight(), d);
        // create the image
        int w = (int) (img.getWidth() * factorX);
        int h = (int) (img.getHeight() * factorY);
        BufferedImage scaled = new BufferedImage(w, h,
                BufferedImage.TYPE_INT_RGB);

        Graphics2D g = scaled.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(img, 0, 0, w, h, null);
        g.dispose();
        img.flush();
        return scaled;
    }

    private float getBinFactor(int width, int height, Dimension dim) {
        float factor = 1;
        float target = getFactor(width, height, dim);
        if (target <= 1) { while (factor / 2 > target) { factor /= 2; }
        } else { while (factor * 2 < target) { factor *= 2; }         }
        return factor;
    }

    private float getFactor(int width, int height, Dimension dim) {
        float sx = dim.width / (float) width;
        float sy = dim.height / (float) height;
        return Math.min(sx, sy);
    }
    
    private float getFactorX(int width, Dimension dim) {
        return dim.width / (float) width;
    }
        
    private float getFactorY(int height, Dimension dim) {
        return dim.height / (float) height;
    }
}
