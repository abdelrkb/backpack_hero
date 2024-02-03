
package sae.map;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//Debut , couloir ,healer ennemi, tresor, marchand, fin
public class Map {
	private static int[][] map = new int[5][11];
//0 = endroit inexistant
//1 = Debut de la carte
//2 = Couloir
//3 = Healer
//4 = ennemi
//5 = tresor
//6 = marchand
//7 Fin

	public Map() {
		List<Integer[]> lstCouloir = new ArrayList<>();
		for (int rows = 0; rows < 5; rows++) {
			for (int cols = 0; cols < 11; cols++) {
				map[rows][cols] = 0;
			}
		}
		map[0][0] = 1;
		// Position de départ
		int x = 0;
		int y = 0;

		// Tracer le chemin aléatoire
		Random random = new Random();
		while (x < 12) {
			map[x][y] = 2;

			// Liste des mouvements possibles
			String[] mvmts = new String[3];
			int cmpt = 0;

			// Vérifier les mouvements possibles (bas, droite)
			if (x < 4 && map[x + 1][y] == 0) {
				mvmts[cmpt++] = "bas";
			}
			if (x > 0 && map[x - 1][y] == 0) {
				mvmts[cmpt++] = "haut";
			}
			if (y < 10 && map[x][y + 1] == 0) {
				mvmts[cmpt++] = "droite";
			}

			// Vérifier si le chemin est bloqué
			if (cmpt == 0) {
				break;
			}
			lstCouloir.add(new Integer[] { x, y });
			// Choisir un mouvement aléatoire parmi les mouvements possibles
			String mouvement = mvmts[random.nextInt(cmpt)];

			// Mettre à jour la position
			if (mouvement.equals("bas")) {
				x++;
			} else if (mouvement.equals("droite")) {
				y++;
			} else if (mouvement.equals("haut")) {
				x--;

				lstCouloir.add(new Integer[] { x, y });
			}
		}

		map[x][y] = 7;
		System.out.println(lstCouloir);
		// On met un healer ds la map
		int couloir = lstCouloir.size();
		if (couloir > 0) {
			int rand = random.nextInt(couloir);
			Integer[] pos = lstCouloir.remove(rand);
			if (map[pos[0]][pos[1]] != 7) {
				map[pos[0]][pos[1]] = 3;
				couloir -= 1;
			}
		}

		couloir = lstCouloir.size();
		// On met deux ennemis dans la map
		if (couloir > 0) {
			for (int i = 0; i < 2; i++) {
				int rand = random.nextInt(couloir);
				Integer[] pos = lstCouloir.remove(rand);
				if (map[pos[0]][pos[1]] != 7) {
					map[pos[0]][pos[1]] = 4;
					couloir -= 1;
				}
				else {
					continue;
				}

				}
		}

		couloir = lstCouloir.size();
		// On met deux tresor dans la map
		if (couloir > 0) {
			for (int i = 0; i < 2; i++) {
				int rand = random.nextInt(couloir);
				Integer[] pos = lstCouloir.remove(rand);
				if (map[pos[0]][pos[1]] != 7) {
				map[pos[0]][pos[1]] = 5;
				couloir -= 1;}
				else {
					continue;
				}
			}
		}

		couloir = lstCouloir.size();
		// On met un marchand sur la map
		if (couloir > 0) {
			int rand = random.nextInt(couloir);
			Integer[] pos = lstCouloir.remove(rand);
			if (map[pos[0]][pos[1]] != 7) {
			map[pos[0]][pos[1]] = 6;
			couloir -= 1;}
		}
		map[0][0] = 1;

		/*
		 * for (int i = 0; i < 5; i++) {
		 * 
		 * if (map[i][j] == 2) { // Générer un nombre aléatoire entre 0 et 1 int
		 * randomChoice = random.nextInt(2); if (randomChoice == 0) { // Modifier le
		 * couloir avec un nombre aléatoire entre 3 et 5 inclus int randomValue =
		 * random.nextInt(3) + 3; map[i][j] = randomValue; } // Sinon, laisser le
		 * couloir tel quel avec la valeur 2 } } }
		 */
	}

	public int getIndex(int i, int j) {
		return map[i][j];
	}

	public void changeIndex(int i, int j, int index) {
		map[i][j] = index;
	}

	public String toString() {
		var sb = new StringBuilder();
		for (int rows = 0; rows < 5; rows++) {
			for (int cols = 0; cols < 11; cols++) {
				sb.append(map[rows][cols]).append(" ");
			}
			sb.append("\n");

		}
		return sb.toString();

	}

}

/*
 * Random random = new Random(); while (true) { map[x][y] = 2;
 * 
 * // Liste des mouvements possibles String[] mouvementsPossibles = new
 * String[4]; int count = 0;
 * 
 * // Vérifier les mouvements possibles (haut, bas, gauche, droite) if (x > 0 &&
 * map[x - 1][y] == 0) { mouvementsPossibles[count++] = "haut"; } if (x < 4 &&
 * map[x + 1][y] == 0) { mouvementsPossibles[count++] = "bas"; } if (y > 0 &&
 * map[x][y - 1] == 0) { mouvementsPossibles[count++] = "gauche"; } if (y < 10
 * && map[x][y + 1] == 0) { mouvementsPossibles[count++] = "droite"; }
 * 
 * // Vérifier si le chemin est bloqué if (count == 0) { break; }
 * 
 * // Choisir un mouvement aléatoire parmi les mouvements possibles String
 * mouvement = mouvementsPossibles[random.nextInt(count)];
 * 
 * // Mettre à jour la position if (mouvement.equals("haut")) { x--; } else if
 * (mouvement.equals("bas")) { x++; } else if (mouvement.equals("gauche")) {
 * y--; } else if (mouvement.equals("droite")) { y++; } }
 */