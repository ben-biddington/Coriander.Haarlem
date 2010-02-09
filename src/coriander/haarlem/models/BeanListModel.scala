package coriander.haarlem.models

class BeanListModel(beans : java.util.List[String]) {
	def getBeans = beans
	def getCount = beans.size
}