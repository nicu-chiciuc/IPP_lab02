import java.util.ArrayList;
import java.util.List;

interface RecursiveShape {
	public void draw (int indent);

	public void addShape (RecursiveShape shape);
}

abstract class SimpleShape implements RecursiveShape {
	abstract String getShapeType ();
	Color shapeColor;

	public SimpleShape (Color color) {
		shapeColor = color;
	}

	public void draw (int indent) {
		System.out.printf(String.format("%" + (indent * 4) + "s", ""));
		shapeColor.showSimpleColor();
		System.out.printf(": %s \n", getShapeType());
	}

	public void addShape (RecursiveShape shape) {

	}
}

abstract class SimpleShapeDecorator implements RecursiveShape {
	RecursiveShape simpleShape;

	public SimpleShapeDecorator (RecursiveShape shape) {
		simpleShape = shape;
	}

	public void addShape (RecursiveShape shape) {}
}

class TextShapeDecorator extends SimpleShapeDecorator {
	protected RecursiveShape decoratedShape;

	private String decorationText;

	public TextShapeDecorator (RecursiveShape shape) {
		super(shape);
		decoratedShape = shape;
	}

	public void setText (String text) {
		decorationText = text;
	}

	public void draw (int indent) {
		decoratedShape.draw(indent);

		System.out.printf(String.format("%" + (indent * 4 + 1) + "s", ""));
		System.out.println("has text: " + decorationText);
	}
}

class ScaleShapeDecorator extends SimpleShapeDecorator {
	protected RecursiveShape decoratedShape;

	private float scaleFactor = 1.0f;

	public ScaleShapeDecorator (RecursiveShape shape) {
		super(shape);
		decoratedShape = shape;
	}

	public void setScaleFactor (float scale) {
		scaleFactor = scale;
	}

	public void draw (int indent) {
		decoratedShape.draw(indent);

		System.out.printf(String.format("%" + (indent * 4 + 1) + "s", ""));
		System.out.println("has scale: " + scaleFactor);
	}
}

class GroupShape implements RecursiveShape {
	List<RecursiveShape> childShapes = new ArrayList<RecursiveShape>();

	public void draw (int indent) {
		System.out.printf(String.format("%" + (indent * 4) + "s", "") + "Group Shape\n");

		for (RecursiveShape shape: childShapes)
			shape.draw(indent + 1);
	}

	public void addShape (RecursiveShape shape) {
		childShapes.add(shape);
	}
}

class Rectangle extends SimpleShape {
	public Rectangle (Color color) {
		super(color);
	}
	public String getShapeType () {
		return "Rectangle";		
	}
}

class Circle extends SimpleShape {
	public Circle (Color color) {
		super(color);
	}
	public String getShapeType () {
		return "Circle";		
	}
}

class Pentagon extends SimpleShape {
	public Pentagon (Color color) {
		super(color);
	}
	public String getShapeType () {
		return "Pentagon";		
	}
}

class ColorRGB {
	int red;
	int green;
	int blue;

	public ColorRGB (int r, int g, int b) {
		red = r;
		green = g;
		blue = b;
	}

	public void showColor () {
		System.out.printf("rgb(%d, %d, %d)", red, green, blue);
	}
}

class ColorFactory {
	static Color getRed () {
		return new ColorRed();
	}
	static Color getGreen () {
		return new ColorGreen();
	}
	static Color getBlue () {
		return new ColorBlue();
	}
}

abstract class Color {
	abstract protected ColorRGB getLegacyColor();
	void showSimpleColor () {
		getLegacyColor().showColor();
	}
}

class ColorRed extends Color {
	ColorRGB legacyColor = new ColorRGB(255, 0, 0);

	protected ColorRGB getLegacyColor () {
		return legacyColor;
	}

}
class ColorGreen extends Color {
	ColorRGB legacyColor = new ColorRGB(0, 255, 0);

	protected ColorRGB getLegacyColor () {
		return legacyColor;
	}
	
}
class ColorBlue extends Color {
	ColorRGB legacyColor = new ColorRGB(0, 0, 255);

	protected ColorRGB getLegacyColor () {
		return legacyColor;
	}
		
}

class FacadeDefaultDiagram {
	static RecursiveShape getStandardCircleDiagram () {
		RecursiveShape shape = new GroupShape();
		RecursiveShape shape2 = new GroupShape();
		TextShapeDecorator textDecorated = new TextShapeDecorator(new Rectangle(ColorFactory.getRed()));
		textDecorated.setText("hello");
		ScaleShapeDecorator scaleDecorated = new ScaleShapeDecorator(textDecorated);
		scaleDecorated.setScaleFactor(4.2f);

		shape2.addShape(scaleDecorated);
		shape2.addShape(new Circle(ColorFactory.getBlue()));
		shape.addShape(shape2);
		shape.addShape(new Pentagon(ColorFactory.getGreen()));

		return shape;
	}
}

public class StructuralPatterns {
	public static void main (String args[]) {
		RecursiveShape shape = FacadeDefaultDiagram.getStandardCircleDiagram();

		shape.draw(1);
 
		Color c = ColorFactory.getRed();
		c.showSimpleColor();
	}
}