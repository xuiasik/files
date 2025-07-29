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
/*    */ class SilkPrefilterState
/*    */ {
/* 39 */   final short[] sLTP_shp = new short[512];
/* 40 */   final int[] sAR_shp = new int[17];
/* 41 */   int sLTP_shp_buf_idx = 0;
/* 42 */   int sLF_AR_shp_Q12 = 0;
/* 43 */   int sLF_MA_shp_Q12 = 0;
/* 44 */   int sHarmHP_Q2 = 0;
/* 45 */   int rand_seed = 0;
/* 46 */   int lagPrev = 0;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   void Reset() {
/* 53 */     Arrays.MemSet(this.sLTP_shp, (short)0, 512);
/* 54 */     Arrays.MemSet(this.sAR_shp, 0, 17);
/* 55 */     this.sLTP_shp_buf_idx = 0;
/* 56 */     this.sLF_AR_shp_Q12 = 0;
/* 57 */     this.sLF_MA_shp_Q12 = 0;
/* 58 */     this.sHarmHP_Q2 = 0;
/* 59 */     this.rand_seed = 0;
/* 60 */     this.lagPrev = 0;
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\concentus\SilkPrefilterState.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */