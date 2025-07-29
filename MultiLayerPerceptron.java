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
/*    */ class MultiLayerPerceptron
/*    */ {
/*    */   private static final int MAX_NEURONS = 100;
/*    */   
/*    */   static float tansig_approx(float x) {
/* 45 */     float sign = 1.0F;
/*    */     
/* 47 */     if (x >= 8.0F) {
/* 48 */       return 1.0F;
/*    */     }
/* 50 */     if (x <= -8.0F) {
/* 51 */       return -1.0F;
/*    */     }
/* 53 */     if (x < 0.0F) {
/* 54 */       x = -x;
/* 55 */       sign = -1.0F;
/*    */     } 
/* 57 */     int i = (int)Math.floor((0.5F + 25.0F * x));
/* 58 */     x -= 0.04F * i;
/* 59 */     float y = OpusTables.tansig_table[i];
/* 60 */     float dy = 1.0F - y * y;
/* 61 */     y += x * dy * (1.0F - y * x);
/* 62 */     return sign * y;
/*    */   }
/*    */ 
/*    */   
/*    */   static void mlp_process(MLPState m, float[] input, float[] output) {
/* 67 */     float[] hidden = new float[100];
/* 68 */     float[] W = m.weights;
/* 69 */     int W_ptr = 0;
/*    */     
/*    */     int j;
/* 72 */     for (j = 0; j < m.topo[1]; j++) {
/*    */       
/* 74 */       float sum = W[W_ptr];
/* 75 */       W_ptr++;
/* 76 */       for (int k = 0; k < m.topo[0]; k++) {
/* 77 */         sum += input[k] * W[W_ptr];
/* 78 */         W_ptr++;
/*    */       } 
/* 80 */       hidden[j] = tansig_approx(sum);
/*    */     } 
/*    */     
/* 83 */     for (j = 0; j < m.topo[2]; j++) {
/*    */       
/* 85 */       float sum = W[W_ptr];
/* 86 */       W_ptr++;
/* 87 */       for (int k = 0; k < m.topo[1]; k++) {
/* 88 */         sum += hidden[k] * W[W_ptr];
/* 89 */         W_ptr++;
/*    */       } 
/* 91 */       output[j] = tansig_approx(sum);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\concentus\MultiLayerPerceptron.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */