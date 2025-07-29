/*    */ package de.maxhenkel.voicechat.net;
/*    */ 
/*    */ import net.minecraft.class_2540;
/*    */ import net.minecraft.class_2960;
/*    */ 
/*    */ public class RequestSecretPacket
/*    */   implements Packet<RequestSecretPacket>
/*    */ {
/*  9 */   public static final class_2960 REQUEST_SECRET = new class_2960("voicechat", "request_secret");
/*    */   
/*    */   private int compatibilityVersion;
/*    */ 
/*    */   
/*    */   public RequestSecretPacket() {}
/*    */ 
/*    */   
/*    */   public RequestSecretPacket(int compatibilityVersion) {
/* 18 */     this.compatibilityVersion = compatibilityVersion;
/*    */   }
/*    */   
/*    */   public int getCompatibilityVersion() {
/* 22 */     return this.compatibilityVersion;
/*    */   }
/*    */ 
/*    */   
/*    */   public class_2960 getIdentifier() {
/* 27 */     return REQUEST_SECRET;
/*    */   }
/*    */ 
/*    */   
/*    */   public RequestSecretPacket fromBytes(class_2540 buf) {
/* 32 */     this.compatibilityVersion = buf.readInt();
/* 33 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public void toBytes(class_2540 buf) {
/* 38 */     buf.writeInt(this.compatibilityVersion);
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\net\RequestSecretPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */