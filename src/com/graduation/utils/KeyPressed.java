//package com.graduation.utils;
//import javax.swing.*;
//import java.awt.event.KeyEvent;
//import java.awt.event.KeyListener;
//public class KeyPressed extends JFrame {
//    float max_v = 1.0f;
//    float display_v = (int)Global.getVolume() * 100;
//    VolumeControlListener VCL = new VolumeControlListener();
//    public KeyPressed(){
//        this.setVisible(false);
//        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
//        this.addKeyListener(VCL);
//    }
//
//    private class VolumeControlListener implements KeyListener{
//        @Override
//        public void keyTyped(KeyEvent e) {
//
//        }
//
//        @Override
//        public void keyPressed(KeyEvent e) {
//            if(e.getKeyCode() == KeyEvent.VK_UP){
//                Global.setVolume(Global.getVolume() + 0.1f);
//                System.out.println("Volume is now " + display_v);
//            }
//            if(e.getKeyCode() == KeyEvent.VK_DOWN){
//                Global.setVolume(Global.getVolume() - 0.1f);
//                System.out.println("Volume is now " + display_v);
//            }
//        }
//
//        @Override
//        public void keyReleased(KeyEvent e) {
//
//        }
//    }
//}
//
