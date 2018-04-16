/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package jfoenix.skins;

import com.sun.javafx.scene.control.skin.SliderSkin;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import jfoenix.controls.JFXSlider;
import jfoenix.controls.JFXSlider.IndicatorPosition;

/**
 * @author Shadi Shaheen
 * rework of JFXSliderSkin by extending Java SliderSkin
 * this solves padding and resizing issues
 */
public class JFXSliderSkin extends SliderSkin {


	private Paint thumbColor = Color.valueOf("#0F9D58"), trackColor = Color.valueOf("#CCCCCC");

	private Text sliderValue;
	private StackPane coloredTrack;
	private StackPane thumb;
	private StackPane track;	
	private StackPane animatedThumb;

	private Timeline timeline;

	private double indicatorRotation, horizontalRotation, rotationAngle = 45, shifting;
	private boolean isValid = false;
	
	
	public JFXSliderSkin(JFXSlider slider) {
		super(slider);

		if(!getSkinnable().isShowTickMarks()){
			track =  (StackPane) getChildren().get(0);
			thumb =  (StackPane) getChildren().get(1);
		}else{
			track =  (StackPane) getChildren().get(1);
			thumb =  (StackPane) getChildren().get(2);
		}
		
		track.setBackground(new Background(new BackgroundFill(trackColor, new CornerRadii(5), Insets.EMPTY)));
		thumb.setBackground(new Background(new BackgroundFill(thumbColor, new CornerRadii(20), Insets.EMPTY)));		
		track.setPrefHeight(2);
		track.setPrefWidth(2);
	
		coloredTrack = new StackPane();
		coloredTrack.backgroundProperty().bind(Bindings.createObjectBinding(()->{
			BackgroundFill trackBackgroundFill = track.getBackground().getFills().get(0);
			return new Background(new BackgroundFill(thumb.getBackground().getFills().get(0).getFill(), trackBackgroundFill.getRadii(), trackBackgroundFill.getInsets()));
		}, track.backgroundProperty(), thumb.backgroundProperty()));
		coloredTrack.setMouseTransparent(true);

		sliderValue = new Text();
		sliderValue.setStroke(Color.WHITE);
		sliderValue.setFont(new Font(10));
		sliderValue.getStyleClass().setAll("sliderValue");

		animatedThumb = new StackPane();
		animatedThumb.getChildren().add(sliderValue);
		animatedThumb.setMouseTransparent(true);
		
		getChildren().add(getChildren().indexOf(thumb), coloredTrack);
		getChildren().add(getChildren().indexOf(thumb), animatedThumb);
		
		initListeners();
	}

	@Override
	protected void layoutChildren(double x, double y, double w, double h) {
		super.layoutChildren(x, y, w, h);
		
		boolean horizontal = getSkinnable().getOrientation() == Orientation.HORIZONTAL;
		double width, height, layoutX,layoutY;
		if(horizontal){
			width = thumb.getLayoutX() - snappedLeftInset();
			height = track.getHeight();
			layoutX = track.getLayoutX();
			layoutY = track.getLayoutY();
			animatedThumb.setLayoutX(thumb.getLayoutX() + thumb.getWidth()/2 - animatedThumb.getWidth()/2);
		}else{
			height = track.getLayoutBounds().getMaxY() + track.getLayoutY() -thumb.getLayoutY() - snappedBottomInset();
			width = track.getWidth();
			layoutX = track.getLayoutX();
			layoutY = thumb.getLayoutY();
			animatedThumb.setLayoutY(thumb.getLayoutY() + thumb.getHeight()/2 - animatedThumb.getHeight()/2);
		}
		coloredTrack.resizeRelocate(layoutX, layoutY, width, height);
		
		if(!isValid){
			initializeVariables();
			initAnimation(getSkinnable().getOrientation());
			isValid = true;
		}
	}

	private boolean internalChange = false;
	
	private void initializeVariables() {
				
		shifting = 30 + thumb.getWidth();

		if (getSkinnable().getOrientation() != Orientation.HORIZONTAL) 
			horizontalRotation = -90;

		if (((JFXSlider)getSkinnable()).getIndicatorPosition() != IndicatorPosition.LEFT) {
			indicatorRotation = 180;
			shifting = -shifting;
		}

		sliderValue.setRotate(rotationAngle + indicatorRotation + 3 * horizontalRotation);

		animatedThumb.resize(30, 30);
		animatedThumb.setRotate(-rotationAngle + indicatorRotation + horizontalRotation);
		animatedThumb.backgroundProperty().bind(Bindings.createObjectBinding(() -> new Background(new BackgroundFill(thumb.getBackground().getFills().get(0).getFill(), new CornerRadii(50, 50, 50, 0, true), null)), thumb.backgroundProperty()));
		animatedThumb.setScaleX(0);
		animatedThumb.setScaleY(0);
	}
	

	private void initListeners() {
		// delegate slider mouse events to track node 
		getSkinnable().setOnMousePressed(me -> {
			if(!me.isConsumed()){
				me.consume();
				track.fireEvent(me);
			}
		});
		getSkinnable().setOnMouseReleased(me -> {
			if(!me.isConsumed()){
				me.consume();
				track.fireEvent(me);
			}
		});
		getSkinnable().setOnMouseDragged(me -> {
			if(!me.isConsumed()){
				me.consume();
				track.fireEvent(me);
			}
		});
		
		// animate value node
		track.addEventHandler(MouseEvent.MOUSE_PRESSED, (event)->{
			timeline.setRate(1);
			timeline.play();
		});		
		track.addEventHandler(MouseEvent.MOUSE_RELEASED, (event)->{
			timeline.setRate(-1);
			timeline.play();
		});			
		thumb.addEventHandler(MouseEvent.MOUSE_PRESSED, (event)->{
			timeline.setRate(1);
			timeline.play();
		});		
		thumb.addEventHandler(MouseEvent.MOUSE_RELEASED, (event)->{
			timeline.setRate(-1);
			timeline.play();
		});		

		track.backgroundProperty().addListener((o,oldVal,newVal)-> {
			// prevent internal color change
			if(!internalChange && newVal!=null)
				trackColor = newVal.getFills().get(0).getFill();		
		});
		
		thumb.backgroundProperty().addListener((o,oldVal,newVal)-> {
			// prevent internal color change
			if(!internalChange && newVal!=null){				
				thumbColor = newVal.getFills().get(0).getFill();
				if(getSkinnable().getValue() == getSkinnable().getMin()){
					internalChange = true;
					thumb.setBackground(new Background(new BackgroundFill(trackColor, new CornerRadii(20), Insets.EMPTY)));
					internalChange = false;
				}
			}
		});
		
		sliderValue.textProperty().bind(Bindings.createStringBinding(()->{
			return Math.round(getSkinnable().getValue())+"";
		}, getSkinnable().valueProperty()));

		getSkinnable().valueProperty().addListener((o,oldVal,newVal)->{
			internalChange = true;
			if(getSkinnable().getMin() == newVal.doubleValue()){
				thumb.setBackground(new Background(new BackgroundFill(trackColor, new CornerRadii(20), Insets.EMPTY)));
			}else if(oldVal.doubleValue() == getSkinnable().getMin()){
				thumb.setBackground(new Background(new BackgroundFill(thumbColor, new CornerRadii(20), Insets.EMPTY)));
			}
			internalChange = false;
		});

		getSkinnable().orientationProperty().addListener((o,oldVal,newVal)->{
			initAnimation(newVal);
		});
	}


	private void initAnimation(Orientation orientation){
		double thumbPos, thumbNewPos;
		DoubleProperty layoutProperty;
		
		if(orientation == Orientation.HORIZONTAL){
			if(((JFXSlider)getSkinnable()).getIndicatorPosition() == IndicatorPosition.RIGHT){
				thumbPos = thumb.getLayoutY() - thumb.getHeight();
				thumbNewPos = thumbPos - shifting;	
			}else{
				thumbPos = thumb.getLayoutY() - animatedThumb.getHeight()/2;
				thumbNewPos = thumb.getLayoutY() - animatedThumb.getHeight() - thumb.getHeight();
			}
			layoutProperty = animatedThumb.layoutYProperty();
		}else{			
			if(((JFXSlider)getSkinnable()).getIndicatorPosition() == IndicatorPosition.RIGHT){
				thumbPos = thumb.getLayoutX() - thumb.getWidth();
				thumbNewPos = thumbPos - shifting;	
			}else{
				thumbPos = thumb.getLayoutX() - animatedThumb.getWidth()/2;
				thumbNewPos = thumb.getLayoutX() - animatedThumb.getWidth() - thumb.getWidth();
			}					
			layoutProperty = animatedThumb.layoutXProperty();
		}
		
		timeline = new Timeline(
				new KeyFrame(
						Duration.ZERO,
						new KeyValue(animatedThumb.scaleXProperty(), 0, Interpolator.EASE_BOTH),
						new KeyValue(animatedThumb.scaleYProperty(), 0, Interpolator.EASE_BOTH),
						new KeyValue(layoutProperty, thumbPos, Interpolator.EASE_BOTH)),
				new KeyFrame(
						Duration.seconds(0.2),
						new KeyValue(animatedThumb.scaleXProperty(), 1, Interpolator.EASE_BOTH),
						new KeyValue(animatedThumb.scaleYProperty(), 1, Interpolator.EASE_BOTH),
						new KeyValue(layoutProperty, thumbNewPos, Interpolator.EASE_BOTH)));
	}

}
