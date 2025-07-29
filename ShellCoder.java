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
/*     */ 
/*     */ 
/*     */ class ShellCoder
/*     */ {
/*     */   static void combine_pulses(int[] output, int[] input, int input_ptr, int len) {
/*  50 */     for (int k = 0; k < len; k++) {
/*  51 */       output[k] = input[input_ptr + 2 * k] + input[input_ptr + 2 * k + 1];
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
/*     */   static void combine_pulses(int[] output, int[] input, int len) {
/*  65 */     for (int k = 0; k < len; k++) {
/*  66 */       output[k] = input[2 * k] + input[2 * k + 1];
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void encode_split(EntropyCoder psRangeEnc, int p_child1, int p, short[] shell_table) {
/*  76 */     if (p > 0) {
/*  77 */       psRangeEnc.enc_icdf(p_child1, shell_table, SilkTables.silk_shell_code_table_offsets[p], 8);
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
/*     */   
/*     */   static void decode_split(short[] p_child1, int child1_ptr, short[] p_child2, int p_child2_ptr, EntropyCoder psRangeDec, int p, short[] shell_table) {
/*  97 */     if (p > 0) {
/*  98 */       p_child1[child1_ptr] = (short)psRangeDec.dec_icdf(shell_table, SilkTables.silk_shell_code_table_offsets[p], 8);
/*  99 */       p_child2[p_child2_ptr] = (short)(p - p_child1[child1_ptr]);
/*     */     } else {
/* 101 */       p_child1[child1_ptr] = 0;
/* 102 */       p_child2[p_child2_ptr] = 0;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void silk_shell_encoder(EntropyCoder psRangeEnc, int[] pulses0, int pulses0_ptr) {
/* 112 */     int[] pulses1 = new int[8];
/* 113 */     int[] pulses2 = new int[4];
/* 114 */     int[] pulses3 = new int[2];
/* 115 */     int[] pulses4 = new int[1];
/*     */ 
/*     */     
/* 118 */     Inlines.OpusAssert(true);
/*     */ 
/*     */     
/* 121 */     combine_pulses(pulses1, pulses0, pulses0_ptr, 8);
/* 122 */     combine_pulses(pulses2, pulses1, 4);
/* 123 */     combine_pulses(pulses3, pulses2, 2);
/* 124 */     combine_pulses(pulses4, pulses3, 1);
/*     */     
/* 126 */     encode_split(psRangeEnc, pulses3[0], pulses4[0], SilkTables.silk_shell_code_table3);
/*     */     
/* 128 */     encode_split(psRangeEnc, pulses2[0], pulses3[0], SilkTables.silk_shell_code_table2);
/*     */     
/* 130 */     encode_split(psRangeEnc, pulses1[0], pulses2[0], SilkTables.silk_shell_code_table1);
/* 131 */     encode_split(psRangeEnc, pulses0[pulses0_ptr], pulses1[0], SilkTables.silk_shell_code_table0);
/* 132 */     encode_split(psRangeEnc, pulses0[pulses0_ptr + 2], pulses1[1], SilkTables.silk_shell_code_table0);
/*     */     
/* 134 */     encode_split(psRangeEnc, pulses1[2], pulses2[1], SilkTables.silk_shell_code_table1);
/* 135 */     encode_split(psRangeEnc, pulses0[pulses0_ptr + 4], pulses1[2], SilkTables.silk_shell_code_table0);
/* 136 */     encode_split(psRangeEnc, pulses0[pulses0_ptr + 6], pulses1[3], SilkTables.silk_shell_code_table0);
/*     */     
/* 138 */     encode_split(psRangeEnc, pulses2[2], pulses3[1], SilkTables.silk_shell_code_table2);
/*     */     
/* 140 */     encode_split(psRangeEnc, pulses1[4], pulses2[2], SilkTables.silk_shell_code_table1);
/* 141 */     encode_split(psRangeEnc, pulses0[pulses0_ptr + 8], pulses1[4], SilkTables.silk_shell_code_table0);
/* 142 */     encode_split(psRangeEnc, pulses0[pulses0_ptr + 10], pulses1[5], SilkTables.silk_shell_code_table0);
/*     */     
/* 144 */     encode_split(psRangeEnc, pulses1[6], pulses2[3], SilkTables.silk_shell_code_table1);
/* 145 */     encode_split(psRangeEnc, pulses0[pulses0_ptr + 12], pulses1[6], SilkTables.silk_shell_code_table0);
/* 146 */     encode_split(psRangeEnc, pulses0[pulses0_ptr + 14], pulses1[7], SilkTables.silk_shell_code_table0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void silk_shell_decoder(short[] pulses0, int pulses0_ptr, EntropyCoder psRangeDec, int pulses4) {
/* 157 */     short[] pulses1 = new short[8];
/* 158 */     short[] pulses2 = new short[4];
/* 159 */     short[] pulses3 = new short[2];
/*     */ 
/*     */     
/* 162 */     Inlines.OpusAssert(true);
/*     */     
/* 164 */     decode_split(pulses3, 0, pulses3, 1, psRangeDec, pulses4, SilkTables.silk_shell_code_table3);
/*     */     
/* 166 */     decode_split(pulses2, 0, pulses2, 1, psRangeDec, pulses3[0], SilkTables.silk_shell_code_table2);
/*     */     
/* 168 */     decode_split(pulses1, 0, pulses1, 1, psRangeDec, pulses2[0], SilkTables.silk_shell_code_table1);
/* 169 */     decode_split(pulses0, pulses0_ptr, pulses0, pulses0_ptr + 1, psRangeDec, pulses1[0], SilkTables.silk_shell_code_table0);
/* 170 */     decode_split(pulses0, pulses0_ptr + 2, pulses0, pulses0_ptr + 3, psRangeDec, pulses1[1], SilkTables.silk_shell_code_table0);
/*     */     
/* 172 */     decode_split(pulses1, 2, pulses1, 3, psRangeDec, pulses2[1], SilkTables.silk_shell_code_table1);
/* 173 */     decode_split(pulses0, pulses0_ptr + 4, pulses0, pulses0_ptr + 5, psRangeDec, pulses1[2], SilkTables.silk_shell_code_table0);
/* 174 */     decode_split(pulses0, pulses0_ptr + 6, pulses0, pulses0_ptr + 7, psRangeDec, pulses1[3], SilkTables.silk_shell_code_table0);
/*     */     
/* 176 */     decode_split(pulses2, 2, pulses2, 3, psRangeDec, pulses3[1], SilkTables.silk_shell_code_table2);
/*     */     
/* 178 */     decode_split(pulses1, 4, pulses1, 5, psRangeDec, pulses2[2], SilkTables.silk_shell_code_table1);
/* 179 */     decode_split(pulses0, pulses0_ptr + 8, pulses0, pulses0_ptr + 9, psRangeDec, pulses1[4], SilkTables.silk_shell_code_table0);
/* 180 */     decode_split(pulses0, pulses0_ptr + 10, pulses0, pulses0_ptr + 11, psRangeDec, pulses1[5], SilkTables.silk_shell_code_table0);
/*     */     
/* 182 */     decode_split(pulses1, 6, pulses1, 7, psRangeDec, pulses2[3], SilkTables.silk_shell_code_table1);
/* 183 */     decode_split(pulses0, pulses0_ptr + 12, pulses0, pulses0_ptr + 13, psRangeDec, pulses1[6], SilkTables.silk_shell_code_table0);
/* 184 */     decode_split(pulses0, pulses0_ptr + 14, pulses0, pulses0_ptr + 15, psRangeDec, pulses1[7], SilkTables.silk_shell_code_table0);
/*     */   }
/*     */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\concentus\ShellCoder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */