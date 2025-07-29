/*    */ package de.maxhenkel.voicechat.macos;
/*    */ 
/*    */ import com.sun.jna.Platform;
/*    */ import java.util.regex.Matcher;
/*    */ import java.util.regex.Pattern;
/*    */ 
/*    */ 
/*    */ public class VersionCheck
/*    */ {
/* 10 */   public static Pattern VERSIONING_PATTERN = Pattern.compile("^(?<major>\\d+)(?:\\.(?<minor>\\d+)(?:\\.(?<patch>\\d+)){0,1}){0,1}$");
/*    */   
/*    */   public static boolean isMinimumVersion(int major, int minor, int patch) {
/* 13 */     String version = System.getProperty("os.version");
/* 14 */     if (version == null) {
/* 15 */       return true;
/*    */     }
/* 17 */     Matcher matcher = VERSIONING_PATTERN.matcher(version);
/* 18 */     if (!matcher.matches()) {
/* 19 */       return true;
/*    */     }
/* 21 */     String majorGroup = matcher.group("major");
/* 22 */     String minorGroup = matcher.group("minor");
/* 23 */     String patchGroup = matcher.group("patch");
/* 24 */     int actualMajor = (majorGroup == null) ? 0 : Integer.parseInt(majorGroup);
/* 25 */     int actualMinor = (minorGroup == null) ? 0 : Integer.parseInt(minorGroup);
/* 26 */     int actualPatch = (patchGroup == null) ? 0 : Integer.parseInt(patchGroup);
/* 27 */     if (major > actualMajor)
/* 28 */       return false; 
/* 29 */     if (major == actualMajor) {
/* 30 */       if (minor > actualMinor)
/* 31 */         return false; 
/* 32 */       if (minor == actualMinor) {
/* 33 */         return (patch < actualPatch);
/*    */       }
/* 35 */       return true;
/*    */     } 
/*    */     
/* 38 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public static boolean isMacOSNativeCompatible() {
/* 43 */     return (Platform.isMac() && isMinimumVersion(11, 0, 0));
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\macos\VersionCheck.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */