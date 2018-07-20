case class Room
(
  `room_id`: Integer,
  `name`: String,
  `type`: String,
  `role`: String,
  `sticky`: Boolean,
  `unread_num`: Integer,
  `mention_num`: Integer,
  `mytask_num`: Integer,
  `message_num`: Integer,
  `file_num`: Integer,
  `task_num`: Integer,
  `icon_path`: String,
  `last_update_time`: Integer
)
case class Message
(
  `message_id`: String
)
case class RoomId
(
  `room_id`: Integer
)
