package coriander.haarlem.core.scheduling

class TimedDisplaySchedule(clock: Clock) extends DisplaySchedule {
	def isVisible = {
		clock.getHour >= 9 && clock.getHour <= 18
	}
}