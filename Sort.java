/*     */ package de.maxhenkel.voicechat.concentus;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class Sort
/*     */ {
/*     */   static void silk_insertion_sort_increasing(int[] a, int[] idx, int L, int K) {
/*  48 */     Inlines.OpusAssert((K > 0));
/*  49 */     Inlines.OpusAssert((L > 0));
/*  50 */     Inlines.OpusAssert((L >= K));
/*     */     
/*     */     int i;
/*  53 */     for (i = 0; i < K; i++) {
/*  54 */       idx[i] = i;
/*     */     }
/*     */ 
/*     */     
/*  58 */     for (i = 1; i < K; i++) {
/*  59 */       int value = a[i];
/*     */       int j;
/*  61 */       for (j = i - 1; j >= 0 && value < a[j]; j--) {
/*  62 */         a[j + 1] = a[j];
/*     */         
/*  64 */         idx[j + 1] = idx[j];
/*     */       } 
/*     */ 
/*     */       
/*  68 */       a[j + 1] = value;
/*     */       
/*  70 */       idx[j + 1] = i;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  76 */     for (i = K; i < L; i++) {
/*  77 */       int value = a[i];
/*     */       
/*  79 */       if (value < a[K - 1]) {
/*  80 */         int j; for (j = K - 2; j >= 0 && value < a[j]; j--) {
/*  81 */           a[j + 1] = a[j];
/*     */           
/*  83 */           idx[j + 1] = idx[j];
/*     */         } 
/*     */ 
/*     */         
/*  87 */         a[j + 1] = value;
/*     */         
/*  89 */         idx[j + 1] = i;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void silk_insertion_sort_increasing_all_values_int16(short[] a, int L) {
/* 109 */     Inlines.OpusAssert((L > 0));
/*     */ 
/*     */     
/* 112 */     for (int i = 1; i < L; i++) {
/* 113 */       short value = a[i]; int j;
/* 114 */       for (j = i - 1; j >= 0 && value < a[j]; j--) {
/* 115 */         a[j + 1] = a[j];
/*     */       }
/*     */       
/* 118 */       a[j + 1] = value;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void silk_insertion_sort_decreasing_int16(short[] a, int[] idx, int L, int K) {
/* 133 */     Inlines.OpusAssert((K > 0));
/* 134 */     Inlines.OpusAssert((L > 0));
/* 135 */     Inlines.OpusAssert((L >= K));
/*     */     
/*     */     int i;
/* 138 */     for (i = 0; i < K; i++) {
/* 139 */       idx[i] = i;
/*     */     }
/*     */ 
/*     */     
/* 143 */     for (i = 1; i < K; i++) {
/* 144 */       short value = a[i]; int j;
/* 145 */       for (j = i - 1; j >= 0 && value > a[j]; j--) {
/* 146 */         a[j + 1] = a[j];
/*     */         
/* 148 */         idx[j + 1] = idx[j];
/*     */       } 
/*     */       
/* 151 */       a[j + 1] = value;
/*     */       
/* 153 */       idx[j + 1] = i;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 159 */     for (i = K; i < L; i++) {
/* 160 */       short value = a[i];
/* 161 */       if (value > a[K - 1]) {
/* 162 */         int j; for (j = K - 2; j >= 0 && value > a[j]; j--) {
/* 163 */           a[j + 1] = a[j];
/*     */           
/* 165 */           idx[j + 1] = idx[j];
/*     */         } 
/*     */         
/* 168 */         a[j + 1] = value;
/*     */         
/* 170 */         idx[j + 1] = i;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\concentus\Sort.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */