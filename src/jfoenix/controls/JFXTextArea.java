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
package jfoenix.controls;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jfoenix.skins.JFXTextAreaSkin;
import jfoenix.validation.base.ValidatorBase;
import com.sun.javafx.css.converters.BooleanConverter;
import com.sun.javafx.css.converters.PaintConverter;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.CssMetaData;
import javafx.css.PseudoClass;
import javafx.css.SimpleStyleableBooleanProperty;
import javafx.css.SimpleStyleableObjectProperty;
import javafx.css.Styleable;
import javafx.css.StyleableBooleanProperty;
import javafx.css.StyleableObjectProperty;
import javafx.css.StyleableProperty;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.scene.control.TextArea;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/**
 * JFXTextArea is the material design implementation of a text area. 
 * 
 * @author  Shadi Shaheen
 * @version 1.0
 * @since   2016-03-09
 */
public class JFXTextArea extends TextArea{

	/**
     * {@inheritDoc}
     */
	public JFXTextArea() {
		super();
		initialize();
	}

	/**
     * {@inheritDoc}
     */
	public JFXTextArea(String text) {
		super(text);
		initialize();
	}

	/**
     * {@inheritDoc}
     */
	@Override
	protected Skin<?> createDefaultSkin() {
		return new JFXTextAreaSkin(this);
	}

	private void initialize() {
		this.getStyleClass().add(DEFAULT_STYLE_CLASS);
		if(System.getProperty("java.vm.name").toLowerCase().equals("dalvik")){
			this.setStyle("-fx-skin: \"jfoenix.android.skins.JFXTextAreaSkinAndroid\";");
		}
	}
	
	/**
     * Initialize the style class to 'jfx-text-field'.
     *
     * This is the selector class from which CSS can be used to style
     * this control.
     */
	private static final String DEFAULT_STYLE_CLASS = "jfx-text-area";
	
	/***************************************************************************
	 *                                                                         *
	 * Properties                                                              *
	 *                                                                         *
	 **************************************************************************/

	/**
	 * holds the current active validator on the text area in case of validation error 
	 */
	private ReadOnlyObjectWrapper<ValidatorBase> activeValidator = new ReadOnlyObjectWrapper<ValidatorBase>();

	public ValidatorBase getActiveValidator() {
		return activeValidator == null ? null : activeValidator.get();
	}

	public ReadOnlyObjectProperty<ValidatorBase> activeValidatorProperty() {
		return this.activeValidator.getReadOnlyProperty();
	}

	/**
	 * list of validators that will validate the text value upon calling 
	 * {{@link #validate()}
	 */
	private ObservableList<ValidatorBase> validators = FXCollections.observableArrayList();

	public ObservableList<ValidatorBase> getValidators() {
		return validators;
	}

	public void setValidators(ValidatorBase... validators) {
		this.validators.addAll(validators);
	}

	/**
	 * validates the text value using the list of validators provided by the user
	 * {{@link #setValidators(ValidatorBase...)}
	 * @return true if the value is valid else false
	 */
	public boolean validate() {
		for (ValidatorBase validator : validators) {
			if (validator.getSrcControl() == null)
				validator.setSrcControl(this);
			validator.validate();
			if (validator.getHasErrors()) {
				activeValidator.set(validator);
				pseudoClassStateChanged(PSEUDO_CLASS_ERROR, true);
				return false;
			}
		}
		activeValidator.set(null);
		pseudoClassStateChanged(PSEUDO_CLASS_ERROR, false);
		return true;
	}

	/***************************************************************************
	 *                                                                         *
	 * styleable Properties                                                    *
	 *                                                                         *
	 **************************************************************************/
	
	/**
	 * set true to show a float the prompt text when focusing the field
	 */
	private StyleableBooleanProperty labelFloat = new SimpleStyleableBooleanProperty(StyleableProperties.LABEL_FLOAT, JFXTextArea.this, "lableFloat", false);
	
	public final StyleableBooleanProperty labelFloatProperty() {
		return this.labelFloat;
	}

	public final boolean isLabelFloat() {
		return this.labelFloatProperty().get();
	}

	public final void setLabelFloat(final boolean labelFloat) {
		this.labelFloatProperty().set(labelFloat);
	}
	
	/**
	 * default color used when the text area is unfocused
	 */
	private StyleableObjectProperty<Paint> unFocusColor = new SimpleStyleableObjectProperty<Paint>(StyleableProperties.UNFOCUS_COLOR, JFXTextArea.this, "unFocusColor", Color.rgb(77, 77, 77));

	public Paint getUnFocusColor() {
		return unFocusColor == null ? Color.rgb(77, 77, 77) : unFocusColor.get();
	}

	public StyleableObjectProperty<Paint> unFocusColorProperty() {
		return this.unFocusColor;
	}

	public void setUnFocusColor(Paint color) {
		this.unFocusColor.set(color);
	}

	/**
	 * default color used when the text area is focused
	 */
	private StyleableObjectProperty<Paint> focusColor = new SimpleStyleableObjectProperty<Paint>(StyleableProperties.FOCUS_COLOR, JFXTextArea.this, "focusColor", Color.valueOf("#4059A9"));

	public Paint getFocusColor() {
		return focusColor == null ? Color.valueOf("#4059A9") : focusColor.get();
	}

	public StyleableObjectProperty<Paint> focusColorProperty() {
		return this.focusColor;
	}

	public void setFocusColor(Paint color) {
		this.focusColor.set(color);
	}

	private static class StyleableProperties {
		private static final CssMetaData<JFXTextArea, Paint> UNFOCUS_COLOR = new CssMetaData<JFXTextArea, Paint>("-fx-unfocus-color", PaintConverter.getInstance(), Color.rgb(77, 77, 77)) {
			@Override
			public boolean isSettable(JFXTextArea control) {
				return control.unFocusColor == null || !control.unFocusColor.isBound();
			}

			@Override
			public StyleableProperty<Paint> getStyleableProperty(JFXTextArea control) {
				return control.unFocusColorProperty();
			}
		};
		private static final CssMetaData<JFXTextArea, Paint> FOCUS_COLOR = new CssMetaData<JFXTextArea, Paint>("-fx-focus-color", PaintConverter.getInstance(), Color.valueOf("#4059A9")) {
			@Override
			public boolean isSettable(JFXTextArea control) {
				return control.focusColor == null || !control.focusColor.isBound();
			}

			@Override
			public StyleableProperty<Paint> getStyleableProperty(JFXTextArea control) {
				return control.focusColorProperty();
			}
		};
		private static final CssMetaData<JFXTextArea, Boolean> LABEL_FLOAT = new CssMetaData<JFXTextArea, Boolean>("-fx-label-float", BooleanConverter.getInstance(), false) {
			@Override
			public boolean isSettable(JFXTextArea control) {
				return control.labelFloat == null || !control.labelFloat.isBound();
			}

			@Override
			public StyleableBooleanProperty getStyleableProperty(JFXTextArea control) {
				return control.labelFloatProperty();
			}
		};

		private static final List<CssMetaData<? extends Styleable, ?>> CHILD_STYLEABLES;
		static {
			final List<CssMetaData<? extends Styleable, ?>> styleables = new ArrayList<CssMetaData<? extends Styleable, ?>>(Control.getClassCssMetaData());
			Collections.addAll(styleables, UNFOCUS_COLOR, FOCUS_COLOR, LABEL_FLOAT);
			CHILD_STYLEABLES = Collections.unmodifiableList(styleables);
		}
	}

	// inherit the styleable properties from parent
	private List<CssMetaData<? extends Styleable, ?>> STYLEABLES;

	@Override
	public List<CssMetaData<? extends Styleable, ?>> getControlCssMetaData() {
		if (STYLEABLES == null) {
			final List<CssMetaData<? extends Styleable, ?>> styleables = new ArrayList<CssMetaData<? extends Styleable, ?>>(Control.getClassCssMetaData());
			styleables.addAll(getClassCssMetaData());
			styleables.addAll(super.getClassCssMetaData());
			STYLEABLES = Collections.unmodifiableList(styleables);
		}
		return STYLEABLES;
	}

	public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
		return StyleableProperties.CHILD_STYLEABLES;
	}
	
	/**
	 * this style class will be activated when a validation error occurs
	 */
	private static final PseudoClass PSEUDO_CLASS_ERROR = PseudoClass.getPseudoClass("error");
	
}
