package game.entity.movement;

import game.world.Map;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;

public class Node {

	private int x, y;
	private double costToHere, costToGoal;
	private Node parent;

	public Node(int x, int y) {
		this.x = x;
		this.y = y;
		costToHere = 0;
		parent = null;
	}

	public Node(Node parent, int xOffs, int yOffs, double cost) {
		this.parent = parent;
		x = parent.x + xOffs;
		y = parent.y + yOffs;
		costToHere = parent.costToHere + cost;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public double getCostToHere() {
		return costToHere;
	}

	public double getCostToGoal() {
		return costToHere + costToGoal;
	}

	public void calcCostToGoal(int goalX, int goalY) {
		costToGoal = Math.abs(goalX - x) + Math.abs(goalY - y);
	}

	public boolean atGoal(int goalX, int goalY) {
		return x == goalX && y == goalY;
	}

	public Node getParent() {
		return parent;
	}

	public ArrayList<Vector2> buildPath(ArrayList<Vector2> path) {
		if (parent != null) {
			parent.buildPath(path);
			path.add(new Vector2(x * Map.TILE_SIZE, y * Map.TILE_SIZE));
		}

		return path;
	}

}
