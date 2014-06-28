package game.entity.movement;

import game.entity.Entity;
import game.world.Map;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;

public class Pathfinder {

	private static final double SQRT_2 = Math.sqrt(2);

	public static ArrayList<Vector2> aStarSearch(Entity start, Entity goal) {
		Map map = start.getMap();
		
		int startX = (int) Math.floor(start.getBounds().x / Map.TILE_SIZE);
		int startY = (int) Math.floor(start.getBounds().y / Map.TILE_SIZE);
		int goalX = (int) Math.floor(goal.getBounds().x / Map.TILE_SIZE);
		int goalY = (int) Math.floor(goal.getBounds().y / Map.TILE_SIZE);

		if (map.isSolid(startX, startY)) {
			startX = (int) Math.ceil(start.getBounds().x / Map.TILE_SIZE);
			startY = (int) Math.ceil(start.getBounds().y / Map.TILE_SIZE);
		}
		
		if (map.isSolid(goalX, goalY)) {
			goalX = (int) Math.ceil(goal.getBounds().x / Map.TILE_SIZE);
			goalY = (int) Math.ceil(goal.getBounds().y / Map.TILE_SIZE);
		}
		
		return aStarSearch(map, startX, startY, goalX, goalY);
	}

	private static ArrayList<Vector2> aStarSearch(Map map, int startX, int startY, int goalX, int goalY) {
		Node start = new Node(startX, startY);

		ArrayList<Node> openList = new ArrayList<Node>();
		ArrayList<Node> closedList = new ArrayList<Node>();

		openList.add(start);
		Node currentNode = start;

		while (true) {
			if (currentNode.atGoal(goalX, goalY)) {
				ArrayList<Vector2> path = new ArrayList<Vector2>();
				return currentNode.buildPath(path);
			}

			for (int xOffs = -1; xOffs <= 1; xOffs++) {
				NextNode: for (int yOffs = -1; yOffs <= 1; yOffs++) {
					if (xOffs == 0 && yOffs == 0)
						continue;

					double cost = SQRT_2;

					if (xOffs == 0 || yOffs == 0)
						cost = 1;

					Node newNode = new Node(currentNode, xOffs, yOffs, cost);

					if (map.outOfBounds(newNode.getX(), newNode.getY()))
						continue;

					if (map.isSolid(newNode.getX(), newNode.getY()))
						continue;

					for (int i = 0; i < closedList.size(); i++) {
						Node otherNode = closedList.get(i);

						if (otherNode.getX() == newNode.getX() && otherNode.getY() == newNode.getY()) {
							if (otherNode.getCostToHere() <= newNode.getCostToHere())
								continue NextNode;

							closedList.remove(otherNode);
						}
					}

					for (int i = 0; i < openList.size(); i++) {
						Node otherNode = openList.get(i);

						if (otherNode.getX() == newNode.getX() && otherNode.getY() == newNode.getY()) {
							if (otherNode.getCostToHere() <= newNode.getCostToHere())
								continue NextNode;

							openList.remove(otherNode);
						}
					}

					newNode.calcCostToGoal(goalX, goalY);
					openList.add(newNode);
				}

			}

			openList.remove(currentNode);
			closedList.add(currentNode);
			currentNode = null;

			double lowestCost = Double.MAX_VALUE;

			for (int i = 0; i < openList.size(); i++) {
				if (openList.get(i).getCostToGoal() < lowestCost) {
					currentNode = openList.get(i);
					lowestCost = currentNode.getCostToGoal();
				}
			}

			if (currentNode == null)
				break;
		}

		return null;
	}

}
