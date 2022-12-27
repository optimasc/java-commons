package com.optimasc.utils;

public class LinkedListNode
{
     public Object data;
     public LinkedListNode next;

     public LinkedListNode(Object item){
         data=item;
         next=null;
     }
     
     public String toString()
     {
       return data.toString();
     }
 
}
