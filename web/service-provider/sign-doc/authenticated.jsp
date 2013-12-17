<%@ page import="uprove.wallets.WalletsServlet" %>
<%--
  Created by IntelliJ IDEA.
  User: andersfogbunzel
  Date: 15/12/13
  Time: 20.55
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String url = uprove.Helper.getServletPath();

    String attribute_name = request.getParameter("name");
    String attribute_age = request.getParameter("age");
    String attribute_citizenship = request.getParameter("citizenship");

    String css = "danger";
    String headline = "You where rejected access!";
    String message = "Sorry, you where unable to sign the document, because you did not include your name as an attribute.";

    if (attribute_name != null) {
        css = "success";
        headline = "Requirements fulfilled!";
        message = "You successfully signed the following document:";
        String document = WalletsServlet.getDocument();
        message += "<p>" + document + "</p>";
    }

    String receivedAttributes = "<p>We received the following information about you:</p>";
    if (attribute_name != null)
        receivedAttributes += "<p>Your name: "+attribute_name;
    if (attribute_age != null)
        receivedAttributes += "<p>Your age: "+attribute_age;
    if (attribute_citizenship != null)
        receivedAttributes += "<p>Your citizen: "+attribute_citizenship;
%>
<html>
<head>
    <title>Sign a document, image etc. with your token (Verifier)</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <!-- Bootstrap -->
    <link href="../../css/bootstrap.min.flatly.css" rel="stylesheet">
    <link href="../../css/custom.flatly.css" rel="stylesheet">

    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
    <script src="https://oss.maxcdn.com/libs/respond.js/1.3.0/respond.min.js"></script>
    <![endif]-->
</head>
<body>
<nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
    <div class="container">
        <!-- Brand and toggle get grouped for better mobile display -->
        <div class="navbar-header">
            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#navbar-collapse-1">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="#">Document signing (Verifier)</a>
        </div>

        <!-- Collect the nav links, forms, and other content for toggling -->
        <div class="collapse navbar-collapse" id="navbar-collapse-1">
            <ul class="nav navbar-nav navbar-right">
                <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown">Navigation <b class="caret"></b></a>
                    <ul class="dropdown-menu">
                        <li><a href="<%= url %>/prover">Prover</a></li>
                        <li><a href="<%= url %>/prover/generate-tokens">Generate tokens</a></li>
                        <li class="divider"></li>
                        <li><a href="<%= url %>/token-wallets">Token wallets</a></li>
                        <li class="divider"></li>
                        <li><a href="<%= url %>/third-party">Third party (wayf)</a></li>
                        <li class="divider"></li>
                        <li><a href="<%= url %>/service-provider/car-rental">Car rental (Verifier)</a></li>
                        <li><a href="<%= url %>/service-provider/elections">Elections (Verifier)</a></li>
                        <li><a href="<%= url %>/service-provider/danish-tax">Danish tax (Verifier)</a></li>
                        <li><a href="<%= url %>/service-provider/sign-doc">Document signing (Verifier)</a></li>
                    </ul>
                </li>
            </ul>
        </div>
        <!-- /.navbar-collapse -->
    </div>
</nav>

<div class="container">
    <div class="row">
        <div class="col-lg-6">
            <h1>Welcome to document signing</h1>
            <p><a href="../../token-wallets">Go back and try another service provider.</a></p>
            <%= receivedAttributes %>
            <br />
            <div class="alert alert-dismissable alert-<%= css %>">
                <h4><%= headline %></h4>
                <p><%= message %></p>
            </div>
        </div>
        <div class="col-lg-4">
            <h3>So what now?</h3>

            <p>You are now either accepted or denied access to the service by only pass selected information about yourself.</p>
        </div>
    </div>
</div>

<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
<script src="https://code.jquery.com/jquery.js"></script>
<!-- Include all compiled plugins (below), or include individual files as needed -->
<script src="../../js/bootstrap.min.js"></script>
</body>
</html>