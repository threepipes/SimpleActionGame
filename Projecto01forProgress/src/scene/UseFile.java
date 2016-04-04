package scene;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class UseFile {
	
	public static void writeFile(int[][] map, String filename){
		if(map == null) return;
		try {
			FileOutputStream writer = new FileOutputStream(new File(filename));
			writer.write(itob(map[0].length));
			writer.write(itob(map.length));
			int maxX = map[0].length;
			int maxY = map.length;
			for(int i=0; i<maxY; i++){
				for(int j=0; j<maxX; j++){
					writer.write(itob(map[i][j]));
				}
			}
			writer.close();
		} catch (FileNotFoundException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}
	
	public static void writeFile(int[][] map, int[][] mapF, String filename){
		if(map == null) return;
		try {
			FileOutputStream writer = new FileOutputStream(new File(filename));
			writer.write(itob(map[0].length));
			writer.write(itob(map.length));
			int maxX = map[0].length;
			int maxY = map.length;
			for(int i=0; i<maxY; i++){
				for(int j=0; j<maxX; j++){
					writer.write(itob(map[i][j]));
				}
			}

			for(int i=0; i<maxY; i++){
				for(int j=0; j<maxX; j++){
					writer.write(itob(mapF[i][j]));
				}
			}
			writer.close();
		} catch (FileNotFoundException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}
	
//	public static int[][] readFile(String filename){
//		try {
//			FileInputStream reader = new FileInputStream(new File("data/"+filename));
//			byte[] buf = new byte[4];
//			reader.read(buf);
//			int mapx = btoi(buf);
//			reader.read(buf);
//			int mapy = btoi(buf);
//			int[][] map = new int[mapy][mapx];
//			for(int i=0; i<mapy; i++){
//				for(int j=0; j<mapx; j++){
//					if(reader.read(buf) == -1) System.err.println("error file");;
//					map[i][j] = btoi(buf);
//				}
//			}
//			reader.close();
//			return map;
//		} catch (FileNotFoundException e) {
//			// TODO 自動生成された catch ブロック
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO 自動生成された catch ブロック
//			e.printStackTrace();
//		}
//		return null;
//	}
	

	public static int[][][] readFile(String filename){
		try {
			FileInputStream reader = new FileInputStream(new File("data/"+filename));
			byte[] buf = new byte[4];
			reader.read(buf);
			int mapx = btoi(buf);
			reader.read(buf);
			int mapy = btoi(buf);
			int[][] map = new int[mapy][mapx];
			int[][] mapF = new int[mapy][mapx];
			for(int i=0; i<mapy; i++){
				for(int j=0; j<mapx; j++){
					if(reader.read(buf) == -1){
						System.err.println("error file");
						reader.close();
						return null;
					}
					map[i][j] = btoi(buf);
				}
			}
			if(reader.read(buf) != -1)for(int i=0; i<mapy; i++){
				for(int j=0; j<mapx; j++){
					mapF[i][j] = btoi(buf);
					if(reader.read(buf) == -1 && i!= mapy-1 && j != mapx-1){
						System.err.println("error file");
						reader.close();
						return null;
					}
				}
			}
			else{
				for(int i=0; i<mapy; i++)
					for(int j=0; j<mapx; j++)
						mapF[i][j] = -1;
			}
			int[][][] tmp = new int[2][][];
			tmp[0] = map;
			tmp[1] = mapF;
			reader.close();
			return tmp;
		} catch (FileNotFoundException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		return null;
	}

	public static byte[] itob(int data){
		byte[] tmp = new byte[4];
		for(int i=0; i<4; i++){
			tmp[i] = (byte) (data & 0xff);
			data >>= 8;
		}
		
		return tmp;
	}
	
	public static int btoi(byte[] data){
		int tmp = 0;
		for(int i=3; i>=0; i--){
			tmp <<= 8;
			tmp |= data[i] & 0xff;
		}
		
		return tmp;
	}
}
