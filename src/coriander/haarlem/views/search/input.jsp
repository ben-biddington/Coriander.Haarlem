<form action="/log-search.html" method="get">
    <label for="q" value="Search" />
    <input type="text" id="q" name="q" value="${results.query}"/>
    <input type="hidden" id="buildId" name="buildId" value="${buildId}"/>
    <input type="submit" id="go" name="go" value="Go"/>
</form>