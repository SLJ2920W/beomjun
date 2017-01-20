package application.cs.mail.handler.deprecated;

import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Test {

	ReadOnlyIntegerWrapper customerNumber = new ReadOnlyIntegerWrapper(this, "customerNumber", 0);

	StringProperty firstName = new SimpleStringProperty(this, "firstName");

	public final StringProperty firstNameProperty() {
		return this.firstName;
	}

	public final java.lang.String getFirstName() {
		return this.firstNameProperty().get();
	}

	public final void setFirstName(final java.lang.String firstName) {
		this.firstNameProperty().set(firstName);
	}
	
	///////

	public final javafx.beans.property.ReadOnlyIntegerProperty customerNumberProperty() {
		return this.customerNumber.getReadOnlyProperty();
	}

	public final int getCustomerNumber() {
		return this.customerNumberProperty().get();
	}
	
	

	
	

}
