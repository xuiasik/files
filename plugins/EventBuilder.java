/*    */ package de.maxhenkel.voicechat.plugins;
/*    */ 
/*    */ import de.maxhenkel.voicechat.api.events.Event;
/*    */ import java.util.ArrayList;
/*    */ import java.util.HashMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.function.Consumer;
/*    */ import java.util.stream.Collectors;
/*    */ import net.minecraft.class_3545;
/*    */ 
/*    */ 
/*    */ public class EventBuilder
/*    */ {
/* 15 */   private final Map<Class<? extends Event>, List<class_3545<Integer, Consumer<? extends Event>>>> events = new HashMap<>();
/*    */ 
/*    */   
/*    */   public <T extends Event> EventBuilder addEvent(Class<T> eventClass, Consumer<T> event, int priority) {
/* 19 */     List<class_3545<Integer, Consumer<? extends Event>>> eventList = this.events.getOrDefault(eventClass, new ArrayList<>());
/* 20 */     eventList.add(new class_3545(Integer.valueOf(priority), event));
/* 21 */     this.events.put(eventClass, eventList);
/* 22 */     return this;
/*    */   }
/*    */   
/*    */   public Map<Class<? extends Event>, List<Consumer<? extends Event>>> build() {
/* 26 */     Map<Class<? extends Event>, List<Consumer<? extends Event>>> result = new HashMap<>();
/* 27 */     for (Map.Entry<Class<? extends Event>, List<class_3545<Integer, Consumer<? extends Event>>>> entry : this.events.entrySet()) {
/* 28 */       result.put(entry.getKey(), (List<Consumer<? extends Event>>)((List)entry.getValue()).stream().sorted((o1, o2) -> Integer.compare(((Integer)o2.method_15442()).intValue(), ((Integer)o1.method_15442()).intValue())).map(class_3545::method_15441).collect(Collectors.toList()));
/*    */     }
/* 30 */     return result;
/*    */   }
/*    */   
/*    */   public static EventBuilder create() {
/* 34 */     return new EventBuilder();
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\plugins\EventBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */