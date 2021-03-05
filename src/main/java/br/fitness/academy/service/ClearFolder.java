package br.fitness.academy.service;

import java.io.File;

public class ClearFolder {
	public static void remover (File f) {
        if (f.isDirectory()) {
            File[] files = f.listFiles();
            for (int i = 0; i < files.length; ++i) {
                remover (files[i]);
            }
        }
        f.delete();
    }
}
