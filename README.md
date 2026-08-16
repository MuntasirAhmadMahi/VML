# Vehicle Maintenace Log Application

**VML** is a simple vehicle maintenance task management application that allows you to add vehicles and assign maintenance tasks to them. You can create, update, and delete tasks, as well as mark completed tasks as done.

The application also allows you to update a vehicle’s odometer reading. Once the reading is updated, the status of applicable maintenance tasks is automatically recalculated and updated dynamically.

## Running

### Requirements
For compiling and running, you will need `JDK-26` or `higher`, as the project uses several features and functionalities that are only available in `JDK-26` or `later versions`.

## Building and Running

### UNIX

First clone the repo:
`git clone https://github.com/MuntasirAhmadMahi/VML.git`

Change to directory:
`cd VML`

Compile and Run:
`mvn compile exec:java -Dexec.mainClass="com.mam.Main"`

### IDE

After cloning or downloading the repository, you can open it in your favorite IDE that supports Maven, such as `IntelliJ IDEA` or `NetBeans`.

## License

This project is licensed under the **MIT License**.