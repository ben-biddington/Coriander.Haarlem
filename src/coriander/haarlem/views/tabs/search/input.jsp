<form action="/viewLog.html" method="get">
    <label for="q" value="Search" />
    <label for="regex" value="Regular exppression" />
    <input type="text" id="q" name="q" value="${q}"/>
    <input type="hidden" id="buildId" name="buildId" value="${buildId}"/>
    <input type="hidden" id="buildTypeId" name="buildTypeId" value="${buildTypeId}"/>
    <input type="hidden" id="buildNumber" name="buildNumber" value="${buildNumber}"/>
    <input type="hidden" id="tab" name="tab" value="${tab}"/>
    <input type="submit" id="go" name="go" value="Go"/>
    <input type="checkbox" id="regex" name="regex" value="1"/>
</form>