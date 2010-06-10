package coriander.haarlem.models

class BeanListModel(
	beans : java.util.List[String],
	registeredCount : Int,
	sessionInfo : java.util.List[String]
) {
	def getBeans = beans
	def getCount = registeredCount
	def getSessionInfo = sessionInfo
}