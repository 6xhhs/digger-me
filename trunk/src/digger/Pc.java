package digger;

import general.display.MemoryDisplayUpdater;


class Pc {

    DiggerCanvas dig;
    
    MemoryDisplayUpdater displayUpdater;
    int width = 320, height = 200, size = width * height;
    int[] pixels;
    
    // Color palletes (we have two)
    int[][][] pal = {{{0, 0x00, 0xAA, 0xAA},
            {0, 0xAA, 0x00, 0x54},
            {0, 0x00, 0x00,0x00}},
            {{0, 0x54,  0xFF, 0xFF},
            {0,  0xFF, 0x54, 0xFF},
            {0,  0x54, 0x54, 0x54}}};

    Pc(DiggerCanvas d) {
        dig = d;
    }

    void gclear() {
        for (int i = 0; i < size; i++) {
            pixels[i] = 0;
        }
        displayUpdater.newPixels(true);
    }

    long gethrt() {
        return System.currentTimeMillis();
    }

    int getkips() {
        return 0;		// phony
    }

    void ggeti(int x, int y, short[] p, int w, int h) {

        int src = 0;
        int x0 = x & 0xfffc;
        int dest = y * width + x0;

        for (int i = 0; i < h; i++) {
            int d = dest;
            for (int j = 0; j < w; j++) {
                p[src++] = (short) ((((((pixels[d] << 2) | pixels[d + 1]) << 2) | pixels[d + 2]) << 2) | pixels[d + 3]);
                d += 4;
                if (src == p.length) {
                    break;
                }
            }
            if (src == p.length) {
                break;
            }
            dest += width;
        }
        int pw = Math.min(4 * w, width - x0);
        int ph = Math.min(h, height - y);
        if (ph > 0 & pw > 0)
            displayUpdater.newPixels(false, x0, y, pw, ph);
    }

    int ggetpix(int x, int y) {
        int x0 = x & 0xfffc;
        int ofs = width * y + x0;
        int retVal = (((((pixels[ofs] << 2) | pixels[ofs + 1]) << 2) | pixels[ofs + 2]) << 2) | pixels[ofs + 3];
        int pw = Math.min(4, width - x0);
        if (y < height  && pw > 0)
            displayUpdater.newPixels(false, x0, y, pw, 1);
        return retVal;
    }

    void ginit() {
    }

    void ginten(int inten) {
        displayUpdater.switchPallete(inten);
    }

    void gpal(int pal) {
    }

    void gputi(int x, int y, short[] p, int w, int h) {
        gputi(x, y, p, w, h, true);
    }

    void gputi(int x, int y, short[] p, int w, int h, boolean b) {

        int src = 0;
        int x0 = x & 0xfffc;
        int dest = y * width + x0;

        for (int i = 0; i < h; i++) {
            int d = dest;
            for (int j = 0; j < w; j++) {
                short px = p[src++];
                pixels[d + 3] = px & 3;
                px >>= 2;
                pixels[d + 2] = px & 3;
                px >>= 2;
                pixels[d + 1] = px & 3;
                px >>= 2;
                pixels[d] = px & 3;
                d += 4;
                if (src == p.length) {
                    break;
                }
            }
            if (src == p.length) {
                break;
            }
            dest += width;
        }
        int pw = Math.min(4*w, width - x0);
        int ph = Math.min(h, height - y);
        if (pw > 0 && ph > 0)
            displayUpdater.newPixels(false, x0, y, pw, ph);
    }

    void gputim(int x, int y, int ch, int w, int h) {

        short[] spr = cgagrafx1.cgatable[ch * 2];
        short[] msk = cgagrafx1.cgatable[ch * 2 + 1];

        int src = 0;
        int x0 = x & 0xfffc;
        int dest = y * width + x0;

        for (int i = 0; i < h; i++) {
            int d = dest;
            for (int j = 0; j < w; j++) {
                short px = spr[src];
                short mx = msk[src];
                src++;
                if ((mx & 3) == 0) {
                    pixels[d + 3] = px & 3;
                }
                px >>= 2;
                if ((mx & (3 << 2)) == 0) {
                    pixels[d + 2] = px & 3;
                }
                px >>= 2;
                if ((mx & (3 << 4)) == 0) {
                    pixels[d + 1] = px & 3;
                }
                px >>= 2;
                if ((mx & (3 << 6)) == 0) {
                    pixels[d] = px & 3;
                }
                d += 4;
                if (src == spr.length || src == msk.length) {
                    break;
                }
            }
            if (src == spr.length || src == msk.length) {
                break;
            }
            dest += width;
        }
        
        int pw = Math.min(4*w, width - x0);
        int ph = Math.min(h, height - y);
        if (pw > 0 && ph > 0)
            displayUpdater.newPixels(false, x0, y, pw, ph);
    }

    void gtitle() {

        int src = 0,
         dest = 0,
         plus = 0;

        while (true) {

            if (src >= cgagrafx2.cgatitledat.length) {
                break;
            }

            int b = cgagrafx2.cgatitledat[src++],
             l,
             c;

            if (b == 0xfe) {
                l = cgagrafx2.cgatitledat[src++];
                if (l == 0) {
                    l = 256;
                }
                c = cgagrafx2.cgatitledat[src++];
            } else {
                l = 1;
                c = b;
            }

            for (int i = 0; i < l; i++) {
                int px = c,
                 adst = 0;
                if (dest < 32768) {
                    adst = (dest / 320) * 640 + dest % 320;
                } else {
                    adst = 320 + ((dest - 32768) / 320) * 640 + (dest - 32768) % 320;
                }
                pixels[adst + 3] = px & 3;
                px >>= 2;
                pixels[adst + 2] = px & 3;
                px >>= 2;
                pixels[adst + 1] = px & 3;
                px >>= 2;
                pixels[adst + 0] = px & 3;
                dest += 4;
                if (dest >= 65535) {
                    break;
                }
            }

            if (dest >= 65535) {
                break;
            }

        }
        displayUpdater.newPixels(false);
        

    }

    void gwrite(int x, int y, int ch, int c) {
        gwrite(x, y, ch, c, true);
    }

    void gwrite(int x, int y, int ch, int c, boolean upd) {

        int dest = x + y * width,
         ofs = 0,
         color = c & 3;

        ch -= 32;
        if ((ch < 0) || (ch > 0x5f)) {
            return;
        }

        short[] chartab = alpha.ascii2cga[ch];

        if (chartab == null) {
            return;
        }

        for (int i = 0; i < 12; i++) {
            int d = dest;
            for (int j = 0; j < 3; j++) {
                int px = chartab[ofs++];
                pixels[d + 3] = px & color;
                px >>= 2;
                pixels[d + 2] = px & color;
                px >>= 2;
                pixels[d + 1] = px & color;
                px >>= 2;
                pixels[d] = px & color;
                d += 4;
            }
            dest += width;
        }

        if (upd) {
            displayUpdater.newPixels(true,x, y, 12, 12);
        }

    }
}