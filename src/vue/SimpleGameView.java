package vue;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

import sae.map.Map;
import sae.enemy.Enemy;
import sae.equipment.Bag;
import sae.equipment.Equipment;
import sae.equipment.Gold;
import sae.hero.Hero;
import sae.map.*;
import javax.imageio.ImageIO;

import fr.umlv.zen5.ApplicationContext;
import model.SimpleGameData;

public record SimpleGameView() {
	//Ecran de fin
	public static void gameOver(ApplicationContext context) {
		Objects.requireNonNull(context);
		var screenInfo = context.getScreenInfo();
		var width = screenInfo.getWidth();
		var height = screenInfo.getHeight();
		context.renderFrame(graphics -> {
			try {
				drawImage(graphics, ImageIO.read(Files.newInputStream(Path.of("img" + "/" + "gameOver.png"))), -210, 0, 2000,
						height);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
	}
	
	//crée le background de base
	public static void setBg(ApplicationContext context, Hero hero, Bag bag, Gold gold) {
		Objects.requireNonNull(context);
		Objects.requireNonNull(hero);
		Objects.requireNonNull(bag);
		Objects.requireNonNull(gold);

		var screenInfo = context.getScreenInfo();
		var width = screenInfo.getWidth();
		var height = screenInfo.getHeight();
		context.renderFrame(graphics -> {
			graphics.setColor(Color.BLACK);
			graphics.fill(new Rectangle2D.Float(0, 0, width, height));
		});

		context.renderFrame(graphics -> {
			try {
				drawImage(graphics, ImageIO.read(Files.newInputStream(Path.of("img" + "/" + "bg.png"))), -200, 0, 2000,
						height);
				drawImage(graphics, ImageIO.read(Files.newInputStream(Path.of("img" + "/" + "hero.png"))), 150, 570,
						200, 200);
				drawGold(context, gold);
				drawBackpack(context, bag);
				lifeHero(context, hero);
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		});
	}
	
	//dessine le sac
	public static void drawBackpack(ApplicationContext context, Bag bag) {
		Objects.requireNonNull(context);
		Objects.requireNonNull(bag);
		var screenInfo = context.getScreenInfo();
		var width = screenInfo.getWidth();
		var height = screenInfo.getHeight();
		context.renderFrame(graphics -> {
			try {
				drawImage(graphics, ImageIO.read(Files.newInputStream(Path.of("img" + "/" + "bagpack.png"))), (width/2)-350,-100, 700,
						600);
				for (int rows = 0; rows < bag.getRows(); rows ++) {
					for (int cols = 0; cols < bag.getCols(); cols ++) {
						drawImage(graphics, ImageIO.read(Files.newInputStream(Path.of("img" + "/" + "cellBag.png"))), (width/2)-200 + cols * 75, 55 + rows * 75, 75,
						75);
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				for (int rows = 0; rows < bag.getRows(); rows ++) {
					for (int cols = 0; cols < bag.getCols(); cols ++) {
						if (bag.getIndex(rows,cols) == 1) {
							drawImage(graphics, ImageIO.read(Files.newInputStream(Path.of("img" + "/" + "sword.png"))), (width/2)-200 + cols * 75, 55 + rows * 75, 75,
									75);
						}
						if (bag.getIndex(rows,cols) == 2) {
							drawImage(graphics, ImageIO.read(Files.newInputStream(Path.of("img" + "/" + "shield.png"))), (width/2)-200 + cols * 75, 55 + rows * 75, 75,
									75);
						}
						if (bag.getIndex(rows,cols) == 3) {
							drawImage(graphics, ImageIO.read(Files.newInputStream(Path.of("img" + "/" + "food.png"))), (width/2)-200 + cols * 75, 55 + rows * 75, 75,
									75);
						}
					}
					
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
	}
	
	//affiche la vie du héros
	public static void lifeHero(ApplicationContext context, Hero hero) {
		Objects.requireNonNull(context);
		Objects.requireNonNull(hero);

		context.renderFrame(graphics -> {
			try {
				drawImage(graphics, ImageIO.read(Files.newInputStream(Path.of("img" + "/" + "heart.png"))), 270,500,
						100, 100);
				drawString(graphics, hero.getLife() + "/40", 300, 550);
				drawImage(graphics, ImageIO.read(Files.newInputStream(Path.of("img" + "/" + "sp.png"))), 120,500,
						100, 100);
				drawString(graphics, hero.getProtection() + "", 165, 555);

			} catch (IOException e) {
				e.printStackTrace();
			}
			
		});

	}
	
	//Afficher le chemin de la map
	public static void chemin(ApplicationContext context, Map map) {
		Objects.requireNonNull(context);
		Objects.requireNonNull(map);
		context.renderFrame(graphics -> {
			var screenInfo = context.getScreenInfo();
			var width = screenInfo.getWidth();
			var height = screenInfo.getHeight();
			for (int rows = 0; rows < 5; rows++) {
				for (int cols = 0; cols < 11; cols++) {
					if (map.getIndex(rows, cols) == 1) {
						graphics.setColor(Color.BLUE);
						graphics.fill(new Rectangle2D.Float(200 + cols * 100, 200 + rows * 100, 100, 100));
					}
					if (map.getIndex(rows, cols) == 2) {
						graphics.setColor(Color.GRAY);
						graphics.fill(new Rectangle2D.Float(200 + cols * 100, 200 + rows * 100, 100, 100));
					}
					if (map.getIndex(rows, cols) == 3) {
						graphics.setColor(Color.GREEN);
						graphics.fill(new Rectangle2D.Float(200 + cols * 100, 200 + rows * 100, 100, 100));
						try {
							drawImage(graphics, ImageIO.read(Files.newInputStream(Path.of("img" + "/" + "healing.png"))), 200 + cols * 100, 200 + rows * 100, 100, 100);
						} catch (IOException e) {
							e.printStackTrace();

						}
						
					}
					
					if (map.getIndex(rows, cols) == 4) {
						graphics.setColor(Color.RED);
						graphics.fill(new Rectangle2D.Float(200 + cols * 100, 200 + rows * 100, 100, 100));
						try {
							drawImage(graphics, ImageIO.read(Files.newInputStream(Path.of("img" + "/" + "fight.png"))),
									200 + cols * 100, 200 + rows * 100, 100, 100);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					if (map.getIndex(rows, cols) == 5) {
						graphics.setColor(Color.YELLOW);
						graphics.fill(new Rectangle2D.Float(200 + cols * 100, 200 + rows * 100, 100, 100));
						try {
							drawImage(graphics, ImageIO.read(Files.newInputStream(Path.of("img" + "/" + "treasureIcon.png"))), 200 + cols * 100, 200 + rows * 100, 100, 100);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}

					if (map.getIndex(rows, cols) == 6) {
						graphics.setColor(Color.PINK);
						graphics.fill(new Rectangle2D.Float(200 + cols * 100, 200 + rows * 100, 100, 100));
						try {
							drawImage(graphics, ImageIO.read(Files.newInputStream(Path.of("img" + "/" + "trade.png"))), 200 + cols * 100, 200 + rows * 100, 100, 100);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}

					if (map.getIndex(rows, cols) == 7) {
						graphics.setColor(Color.ORANGE);
						graphics.fill(new Rectangle2D.Float(200 + cols * 100, 200 + rows * 100, 100, 100));
						try {
							drawImage(graphics, ImageIO.read(Files.newInputStream(Path.of("img" + "/" + "door.png"))), 200 + cols * 100, 200 + rows * 100, 100, 100);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}

				}
			}
		});
	}

	//afficher le fond de la map
	public static void bgMap(ApplicationContext context) {
		Objects.requireNonNull(context);
		context.renderFrame(graphics -> {
			var screenInfo = context.getScreenInfo();
			var width = screenInfo.getWidth();
			var height = screenInfo.getHeight();

			Rectangle2D.Float bg = new Rectangle2D.Float(170, 100, 1200, 600);
			graphics.setColor(Color.decode("#B38B6D"));
			graphics.draw(bg);
			graphics.fill(bg);
		});
	}

	//Position du hero sur la carte
	public static void heroMap(ApplicationContext context, int rows, int cols) {
		Objects.requireNonNull(context);
		context.renderFrame(graphics -> {
			try {
				drawImage(graphics, ImageIO.read(Files.newInputStream(Path.of("img" + "/" + "hero.png"))),
						200 + cols * 100, 200 + rows * 100, 100, 100);
			} catch (IOException e) {
				e.printStackTrace();
			}

		});
	}

	//Pour ouvrir la map
	public static void drawMap(ApplicationContext context, Map map, int rows, int cols) {
		Objects.requireNonNull(context);
		Objects.requireNonNull(map);
		bgMap(context);
		chemin(context, map);
		heroMap(context, rows, cols);

	};

	//Ecrase la map en redessinant dessus
	public static void deleteMap(ApplicationContext context, Hero hero, Bag bag, Gold gold) {
		Objects.requireNonNull(context);
		Objects.requireNonNull(hero);
		Objects.requireNonNull(bag);
		Objects.requireNonNull(gold);

		setBg(context, hero, bag, gold);
		drawBackpack(context, bag);
	}


	//Afficher la map du healer
	public static void merchantMap(ApplicationContext context, Hero hero, Bag bag, Gold gold) {
		Objects.requireNonNull(context);
		Objects.requireNonNull(hero);
		Objects.requireNonNull(bag);
		Objects.requireNonNull(gold);

		deleteMap(context, hero, bag, gold);
		var screenInfo = context.getScreenInfo();
		var width = screenInfo.getWidth();
		var height = screenInfo.getHeight();
		context.renderFrame(graphics -> {
			graphics.setColor(Color.BLACK);
			graphics.fill(new Rectangle2D.Float(0, 0, width, height));
		});

		context.renderFrame(graphics -> {
			try {
				drawImage(graphics, ImageIO.read(Files.newInputStream(Path.of("img" + "/" + "bg.png"))), -200, 0, 2000,
						height);
				drawImage(graphics, ImageIO.read(Files.newInputStream(Path.of("img" + "/" + "hero.png"))), 150, 570,
						200, 200);
				lifeHero(context, hero);
				drawImage(graphics, ImageIO.read(Files.newInputStream(Path.of("img" + "/" + "merchant.png"))), 1000, 570,
						200, 200);
				drawGold(context, gold);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		drawBackpack(context, bag);

	}
	
	//Pour afficher la map du healer
	public static void healerMap(ApplicationContext context, Hero hero, Bag bag, Gold gold) {
		Objects.requireNonNull(context);
		Objects.requireNonNull(hero);
		Objects.requireNonNull(bag);
		Objects.requireNonNull(gold);

		deleteMap(context, hero, bag, gold);
		var screenInfo = context.getScreenInfo();
		var width = screenInfo.getWidth();
		var height = screenInfo.getHeight();
		context.renderFrame(graphics -> {
			graphics.setColor(Color.BLACK);
			graphics.fill(new Rectangle2D.Float(0, 0, width, height));
		});

		context.renderFrame(graphics -> {
			try {
				drawImage(graphics, ImageIO.read(Files.newInputStream(Path.of("img" + "/" + "bg.png"))), -200, 0, 2000,
						height);
				drawImage(graphics, ImageIO.read(Files.newInputStream(Path.of("img" + "/" + "hero.png"))), 150, 570,
						200, 200);
				lifeHero(context, hero);
				drawImage(graphics, ImageIO.read(Files.newInputStream(Path.of("img" + "/" + "healer.png"))), 1000, 570,
						200, 200);
				drawGold(context, gold);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		drawBackpack(context, bag);

	}

	public static void drawEnergy(ApplicationContext context, int energy) {
		Objects.requireNonNull(context);
		context.renderFrame(graphics -> {
			try {
				drawImage(graphics,ImageIO.read(Files.newInputStream(Path.of("img" + "/" + "energy.png"))) ,100, 650,
						75, 75);
				graphics.setColor(Color.WHITE);
				drawString(graphics, energy+"", 140, 710);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
	}
	
	//Pour afficher la map enemy
	public static void enemyMap(ApplicationContext context, Hero hero, Bag bag, Enemy enemy, Gold gold, int energy) {
		Objects.requireNonNull(context);
		Objects.requireNonNull(hero);
		Objects.requireNonNull(bag);
		deleteMap(context, hero, bag, gold);
		var name = enemy.toString();
		var screenInfo = context.getScreenInfo();
		var width = screenInfo.getWidth();
		var height = screenInfo.getHeight();
		context.renderFrame(graphics -> {
			graphics.setColor(Color.BLACK);
			graphics.fill(new Rectangle2D.Float(0, 0, width, height));
		});

		context.renderFrame(graphics -> {
			try {
				drawImage(graphics, ImageIO.read(Files.newInputStream(Path.of("img" + "/" + "bg.png"))), -200, 0, 2000,
						height);
				drawImage(graphics, ImageIO.read(Files.newInputStream(Path.of("img" + "/" + "hero.png"))), 150, 570,
						200, 200);
				drawEnergy(context, energy);
				lifeHero(context, hero);
				drawImage(graphics, ImageIO.read(Files.newInputStream(Path.of("img" + "/" + name+".png"))), 1050, 570,
						200, 200);
				drawImage(graphics, ImageIO.read(Files.newInputStream(Path.of("img" + "/" + "heart.png"))), 960,570,
						100, 100);
				drawString(graphics, enemy.getLifePoint()+"", 1000, 620);
				drawImage(graphics, ImageIO.read(Files.newInputStream(Path.of("img" + "/" + "sp.png"))), 960,650,
						100, 100);
				drawString(graphics, enemy.getShieldPoint()+"", 1001, 707);

				
				
				drawGold(context, gold);

			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		drawBackpack(context, bag);
	}
	
	public static void drawGold(ApplicationContext context, Gold gold) {
		context.renderFrame(graphics -> {
			try {
				drawImage(graphics, ImageIO.read(Files.newInputStream(Path.of("img" + "/" + "coin.png"))), 300, 700,
						50, 50);
				graphics.setColor(Color.WHITE);
				drawString(graphics, gold.getNumberOfGold()+"", 330, 750);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}
	
	//Pour afficher l'evenement tresor
	public static void treasureMap(ApplicationContext context, Hero hero, Bag bag, Gold gold) {
		Objects.requireNonNull(context);
		Objects.requireNonNull(hero);
		Objects.requireNonNull(bag);
		Objects.requireNonNull(gold);

		deleteMap(context, hero, bag, gold);
		var screenInfo = context.getScreenInfo();
		var width = screenInfo.getWidth();
		var height = screenInfo.getHeight();
		context.renderFrame(graphics -> {
			graphics.setColor(Color.BLACK);
			graphics.fill(new Rectangle2D.Float(0, 0, width, height));
		});

		context.renderFrame(graphics -> {
			try {
				drawImage(graphics, ImageIO.read(Files.newInputStream(Path.of("img" + "/" + "bg.png"))), -200, 0, 2000,
						height);
				drawImage(graphics, ImageIO.read(Files.newInputStream(Path.of("img" + "/" + "hero.png"))), 150, 570,
						200, 200);
				lifeHero(context, hero);
				drawImage(graphics, ImageIO.read(Files.newInputStream(Path.of("img" + "/" + "treasure.png"))), width/2, 650,
						150, 150);
				drawGold(context, gold);

				//woodenSword(context, 1000, 100);
				//woodenShield(context, 300,100);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		drawBackpack(context, bag); 

	}
	
	
	//Image arme et items
	public static void woodenSword(ApplicationContext context, float x, float y) {
		Objects.requireNonNull(context);
		context.renderFrame(graphics -> {
			try {
				drawImage(graphics, ImageIO.read(Files.newInputStream(Path.of("img" + "/" + "sword.png"))), x, y,
						200, 200);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}
	
	public static void woodenShield(ApplicationContext context, float shieldX, float shieldY) {
		Objects.requireNonNull(context);
		context.renderFrame(graphics -> {
			try {
				drawImage(graphics, ImageIO.read(Files.newInputStream(Path.of("img" + "/" + "shield.png"))), shieldX, shieldY,
						100, 100);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}
	
	public static void food(ApplicationContext context, float x, float y) {
		Objects.requireNonNull(context);
		context.renderFrame(graphics -> {
			try {
				drawImage(graphics, ImageIO.read(Files.newInputStream(Path.of("img" + "/" + "food.png"))), x, y,
						100, 100);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}
	
	public static void bow(ApplicationContext context, float x, float y) {
		Objects.requireNonNull(context);
		context.renderFrame(graphics -> {
			try {
				drawImage(graphics, ImageIO.read(Files.newInputStream(Path.of("img" + "/" + "food.png"))), x, y,
						100, 100);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}
	
	
	//Methode draw
	private static void drawString(Graphics2D graphics, String str, float x, float y) {
		Objects.requireNonNull(graphics);
		Objects.requireNonNull(str);
		graphics.drawString(str, x, y);
	}
	private static void drawImage(Graphics2D graphics, BufferedImage image, float x, float y, float dimX, float dimY) {
		Objects.requireNonNull(graphics);
		Objects.requireNonNull(image);
		var width = image.getWidth();
		var height = image.getHeight();
		var scale = Math.min(dimX / width, dimY / height);
		var transform = new AffineTransform(scale, 0, 0, scale, x + (dimX - scale * width) / 2,
				y + (dimY - scale * height) / 2);
		graphics.drawImage(image, transform, null);
	}



}
