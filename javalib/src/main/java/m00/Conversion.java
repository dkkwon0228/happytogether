/*
 * Conversion.java
 *
 * Created on 3 de Janeiro de 2005, 13:32
 */

/**
 *
 * @author  Consularo
 */

package m00;

import java.awt.image.*;

public class Conversion {
    private String message;
    private boolean prc = true;
    private BufferedImage bConv;
    
    /** Creates a new instance of Conversion */
    public Conversion(BufferedImage bSrc) {
        
        bConv = null;
        switch (bSrc.getType()) {
            case BufferedImage.TYPE_BYTE_BINARY: 
                message = "TYPE_BYTE_BINARY. Imagem Binaria. Ja esta limiarizada";
                prc = false;
                break;
            case BufferedImage.TYPE_BYTE_INDEXED: 
                message = "TYPE_BYTE_INDEXED. Imagem em Bytes com indice de Cores. Exige mapeamento.";
                prc = false;
                break;
            case BufferedImage.TYPE_BYTE_GRAY:
                message = "TYPE_BYTE_GRAY. Imagem em Niveis de Cinza. 8 bits sem sinal. Limiarizavel";
                break;
            case BufferedImage.TYPE_USHORT_GRAY:
                message = "TYPE_USHORT_GRAY. Imagem em Niveis de Cinza. 16 bits sem sinal.";
                break;
            case BufferedImage.TYPE_USHORT_555_RGB: 
                message = "TYPE_USHORT_555_RGB. Imagem Colorida em 3 canais. Cada canal com 5 bits e sem sinal.";
                break;
            case BufferedImage.TYPE_USHORT_565_RGB: 
                message = "TYPE_USHORT_565_RGB. Imagem Colorida em 3 canais. Cada canal com 5 bits e sem sinal, mas o canal verde tem 6 bits.";
                break;
            case BufferedImage.TYPE_3BYTE_BGR:
                message = "TYPE_3BYTE_BGR. Imagem Colorida. 3 canais e cada canal com 8 bits sem sinal.";
                break;
            case BufferedImage.TYPE_4BYTE_ABGR:
                message = "TYPE_4BYTE_BGR. Imagem Colorida. 4 canais (um alfa) e cada canal com 8 bits sem sinal.";
                break;
            case BufferedImage.TYPE_4BYTE_ABGR_PRE:
                message = "TYPE_4BYTE_BGR_PRE. Imagem Colorida. 4 canais (um alfa) e cada canal com 8 bits sem sinal.";
                break;
            case BufferedImage.TYPE_INT_RGB:
                message = "TYPE_INT_RGB. Imagem Colorida. 3 canais e cada canal com 8 bits com sinal.";
                break;
            case BufferedImage.TYPE_INT_BGR:
                message = "TYPE_INT_BGR. Imagem Colorida. 3 canais e cada canal com 8 bits com sinal.";
                break;
            case BufferedImage.TYPE_INT_ARGB:
                message = "TYPE_INT_ARGB. Imagem Colorida. 4 canais (um alfa) e cada canal com 8 bits com sinal.";
                break;
            case BufferedImage.TYPE_INT_ARGB_PRE:
                message = "TYPE_INT_ARGB_PRE. Imagem Colorida. 4 canais (um alfa) e cada canal com 8 bits com sinal.";
                break;
            case BufferedImage.TYPE_CUSTOM:
                message = "TYPE_CUSTOM. Tipo proprietario."; 
                prc = false;
                break;
            default:
                message = "Tipo desconhecido.";
                prc = false;
        }
        switch (bSrc.getType()) {
            case BufferedImage.TYPE_BYTE_GRAY:
                bConv = bSrc;
            case BufferedImage.TYPE_USHORT_GRAY:
                bConv = bSrc;
                break;
            case BufferedImage.TYPE_USHORT_555_RGB: 
            case BufferedImage.TYPE_USHORT_565_RGB: 
            case BufferedImage.TYPE_3BYTE_BGR:
            case BufferedImage.TYPE_4BYTE_ABGR:
            case BufferedImage.TYPE_4BYTE_ABGR_PRE:
            case BufferedImage.TYPE_INT_RGB:
            case BufferedImage.TYPE_INT_BGR:
            case BufferedImage.TYPE_INT_ARGB:
            case BufferedImage.TYPE_INT_ARGB_PRE:
                bConv = new BufferedImage(bSrc.getWidth(), bSrc.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
                WritableRaster input = bSrc.copyData(null);
                WritableRaster output = bConv.getRaster();
                for (int y=0; y<bSrc.getHeight(); ++y)
                    for (int x=0; x<bSrc.getWidth(); ++x)
                        output.setSample(x, y, 0, (input.getSample(x, y, 0) + input.getSample(x, y, 1) + input.getSample(x, y, 2))/3);
                break;
        }
    }

    public Conversion(BufferedImage bSrc, boolean asColor) {
        
        bConv = null;
        switch (bSrc.getType()) {
            case BufferedImage.TYPE_BYTE_BINARY: 
                message = "TYPE_BYTE_BINARY. Imagem Binaria. Ja esta limiarizada";
                prc = false;
                break;
            case BufferedImage.TYPE_BYTE_INDEXED: 
                message = "TYPE_BYTE_INDEXED. Imagem em Bytes com indice de Cores. Exige mapeamento.";
                prc = false;
                break;
            case BufferedImage.TYPE_BYTE_GRAY:
                message = "TYPE_BYTE_GRAY. Imagem em Niveis de Cinza. 8 bits sem sinal. Limiarizavel";
                break;
            case BufferedImage.TYPE_USHORT_GRAY:
                message = "TYPE_USHORT_GRAY. Imagem em Niveis de Cinza. 16 bits sem sinal.";
                break;
            case BufferedImage.TYPE_USHORT_555_RGB: 
                message = "TYPE_USHORT_555_RGB. Imagem Colorida em 3 canais. Cada canal com 5 bits e sem sinal.";
                break;
            case BufferedImage.TYPE_USHORT_565_RGB: 
                message = "TYPE_USHORT_565_RGB. Imagem Colorida em 3 canais. Cada canal com 5 bits e sem sinal, mas o canal verde tem 6 bits.";
                break;
            case BufferedImage.TYPE_3BYTE_BGR:
                message = "TYPE_3BYTE_BGR. Imagem Colorida. 3 canais e cada canal com 8 bits sem sinal.";
                break;
            case BufferedImage.TYPE_4BYTE_ABGR:
                message = "TYPE_4BYTE_BGR. Imagem Colorida. 4 canais (um alfa) e cada canal com 8 bits sem sinal.";
                break;
            case BufferedImage.TYPE_4BYTE_ABGR_PRE:
                message = "TYPE_4BYTE_BGR_PRE. Imagem Colorida. 4 canais (um alfa) e cada canal com 8 bits sem sinal.";
                break;
            case BufferedImage.TYPE_INT_RGB:
                message = "TYPE_INT_RGB. Imagem Colorida. 3 canais e cada canal com 8 bits com sinal.";
                break;
            case BufferedImage.TYPE_INT_BGR:
                message = "TYPE_INT_BGR. Imagem Colorida. 3 canais e cada canal com 8 bits com sinal.";
                break;
            case BufferedImage.TYPE_INT_ARGB:
                message = "TYPE_INT_ARGB. Imagem Colorida. 4 canais (um alfa) e cada canal com 8 bits com sinal.";
                break;
            case BufferedImage.TYPE_INT_ARGB_PRE:
                message = "TYPE_INT_ARGB_PRE. Imagem Colorida. 4 canais (um alfa) e cada canal com 8 bits com sinal.";
                break;
            case BufferedImage.TYPE_CUSTOM:
                message = "TYPE_CUSTOM. Tipo proprietario."; 
                prc = false;
bConv = bSrc;
                break;
            default:
                message = "Tipo desconhecido.";
                prc = false;
        }
//System.out.println(message);
        switch (bSrc.getType()) {
            case BufferedImage.TYPE_BYTE_GRAY:
            case BufferedImage.TYPE_USHORT_GRAY:
                if (!asColor)
                    bConv = bSrc;
                else {
                    bConv = new BufferedImage(bSrc.getWidth(), bSrc.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
                    WritableRaster input = bSrc.copyData(null);
                    WritableRaster output = bConv.getRaster();
                    for (int y=0; y<bSrc.getHeight(); ++y)
                        for (int x=0; x<bSrc.getWidth(); ++x) {
                            output.setSample(x, y, 0, input.getSample(x, y, 0));
                            output.setSample(x, y, 1, input.getSample(x, y, 0));
                            output.setSample(x, y, 2, input.getSample(x, y, 0));
                        }
                }
                break;
            case BufferedImage.TYPE_INT_ARGB:
            case BufferedImage.TYPE_INT_ARGB_PRE:
            case BufferedImage.TYPE_USHORT_555_RGB: 
            case BufferedImage.TYPE_USHORT_565_RGB: 
            case BufferedImage.TYPE_INT_RGB:
                if (!asColor) {
                    bConv = new BufferedImage(bSrc.getWidth(), bSrc.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
                    WritableRaster input = bSrc.copyData(null);
                    WritableRaster output = bConv.getRaster();
                    for (int y=0; y<bSrc.getHeight(); ++y)
                        for (int x=0; x<bSrc.getWidth(); ++x)
                            output.setSample(x, y, 0, (input.getSample(x, y, 0) + input.getSample(x, y, 1) + input.getSample(x, y, 2))/3);
                }
                else {
                    bConv = new BufferedImage(bSrc.getWidth(), bSrc.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
                    WritableRaster input = bSrc.copyData(null);
                    WritableRaster output = bConv.getRaster();
                    for (int y=0; y<bSrc.getHeight(); ++y)
                        for (int x=0; x<bSrc.getWidth(); ++x) {
                            output.setSample(x, y, 0, input.getSample(x, y, 2));
                            output.setSample(x, y, 1, input.getSample(x, y, 1));
                            output.setSample(x, y, 2, input.getSample(x, y, 0));
                        }
                }
                break;
            case BufferedImage.TYPE_3BYTE_BGR:
            case BufferedImage.TYPE_4BYTE_ABGR:
            case BufferedImage.TYPE_4BYTE_ABGR_PRE:
            case BufferedImage.TYPE_INT_BGR:
                if (!asColor) {
//System.out.println("Conversion: TYPE_3BYTE_BGR @@@@mean gray");
                    bConv = new BufferedImage(bSrc.getWidth(), bSrc.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
                    WritableRaster input = bSrc.copyData(null);
                    WritableRaster output = bConv.getRaster();
                    for (int y=0; y<bSrc.getHeight(); ++y)
                        for (int x=0; x<bSrc.getWidth(); ++x)
                            output.setSample(x, y, 0, (input.getSample(x, y, 0) + input.getSample(x, y, 1) + input.getSample(x, y, 2))/3);
                }
                else {
//System.out.println("Conversion: TYPE_3BYTE_BGR @@@@color");
                    bConv = new BufferedImage(bSrc.getWidth(), bSrc.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
                    WritableRaster input = bSrc.copyData(null);
                    WritableRaster output = bConv.getRaster();
                    for (int y=0; y<bSrc.getHeight(); ++y)
                        for (int x=0; x<bSrc.getWidth(); ++x) {
                            output.setSample(x, y, 0, input.getSample(x, y, 0));
                            output.setSample(x, y, 1, input.getSample(x, y, 1));
                            output.setSample(x, y, 2, input.getSample(x, y, 2));
                        }
                }
                break;
        }
    }
    
    public String getMessage() {
        return(message);
    }
    
    public boolean canProceed() {
        return(prc);
    }
    
    public BufferedImage imgConverted() {
        return(bConv);
    }
}