package com.mam.serializer.custom;

import com.mam.model.MileageTask;
import com.mam.model.Task;
import com.mam.model.TaskKind;
import com.mam.model.TimeTask;
import com.mam.serializer.TaskSerializer;

import java.time.LocalDate;
import java.util.UUID;

public class CustomTaskSerializer implements TaskSerializer<String> {
    @Override
    public String serialize(Task task) {
        if (task instanceof TimeTask timeTask) {
            return serializeTimeTask(timeTask);
        } else if (task instanceof MileageTask mileageTask) {
            return serializeMileageTask(mileageTask);
        } else {
            throw new IllegalArgumentException("Unsupported task type: " + task.getClass());
        }
    }

    private String serializeTimeTask(TimeTask task) {
        CustomFormatWriter writer = new CustomFormatWriter();

        writer.write("T"); // Type
        writer.write(task.getId().toString());
        writer.write(task.getType().name());
        writer.write(task.getName());
        writer.write(task.getNote());
        writer.write(task.getDueDate().toString()); // It cannot be null
        if (task.getSavedDate() == null) { // but it maybe
            writer.write(null);
        } else {
            writer.write(task.getSavedDate().toString());
        }

        return writer.result();
    }

    private String serializeMileageTask(MileageTask task) {
        CustomFormatWriter writer = new CustomFormatWriter();

        writer.write("M"); // Type
        writer.write(task.getId().toString());
        writer.write(task.getType().name());
        writer.write(task.getName());
        writer.write(task.getNote());
        writer.write(task.getDueAtOdometer());
        writer.write(task.getSavedDueAtOdometer());

        return writer.result();
    }

    @Override
    public Task deserialize(String data) {
        CustomFormatReader reader = new CustomFormatReader(data);
        String type = reader.readString(reader.readInt());
        if (type.equals("T")) {
            return deserializeTimeTask(reader);
        } else if (type.equals("M")) {
            return deserializeMileageTask(reader);
        } else {
            throw new RuntimeException("Invalid type '" + type + "'");
        }
    }

    private TimeTask deserializeTimeTask(CustomFormatReader reader) {
        TimeTask timeTask = new TimeTask(
                UUID.fromString(reader.readString(reader.readInt())),
                TaskKind.valueOf(reader.readString(reader.readInt())),
                reader.readString(reader.readInt()),
                reader.readString(reader.readInt()),
                LocalDate.parse(reader.readString(reader.readInt())),
                null
//                LocalDate.parse(reader.readString(reader.readInt())) // Error here It can be null
        );
        int len = reader.readInt();
        if (len > 0) {
            timeTask.setSavedDate(LocalDate.parse(reader.readString(len)));
        }
        return timeTask;
    }

    private MileageTask deserializeMileageTask(CustomFormatReader reader) {
        return new MileageTask(
                UUID.fromString(reader.readString(reader.readInt())),
                TaskKind.valueOf(reader.readString(reader.readInt())),
                reader.readString(reader.readInt()),
                reader.readString(reader.readInt()),
                reader.readInt(),
                reader.readInt()
        );
    }
}
