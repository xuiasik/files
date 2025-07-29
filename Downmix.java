/*    */ package de.maxhenkel.voicechat.concentus;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class Downmix
/*    */ {
/*    */   static void downmix_int(short[] x, int x_ptr, int[] sub, int sub_ptr, int subframe, int offset, int c1, int c2, int C) {
/*    */     int j;
/* 53 */     for (j = 0; j < subframe; j++) {
/* 54 */       sub[j + sub_ptr] = x[(j + offset) * C + c1];
/*    */     }
/* 56 */     if (c2 > -1) {
/* 57 */       for (j = 0; j < subframe; j++) {
/* 58 */         sub[j + sub_ptr] = sub[j + sub_ptr] + x[(j + offset) * C + c2];
/*    */       }
/* 60 */     } else if (c2 == -2) {
/*    */       
/* 62 */       for (int c = 1; c < C; c++) {
/* 63 */         for (j = 0; j < subframe; j++) {
/* 64 */           sub[j + sub_ptr] = sub[j + sub_ptr] + x[(j + offset) * C + c];
/*    */         }
/*    */       } 
/*    */     } 
/* 68 */     int scale = 4096;
/* 69 */     if (C == -2) {
/* 70 */       scale /= C;
/*    */     } else {
/* 72 */       scale /= 2;
/*    */     } 
/* 74 */     for (j = 0; j < subframe; j++)
/* 75 */       sub[j + sub_ptr] = sub[j + sub_ptr] * scale; 
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\concentus\Downmix.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */