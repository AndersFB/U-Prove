<%--
  Created by IntelliJ IDEA.
  User: andersfogbunzel
  Date: 15/12/13
  Time: 20.55
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String url = uprove.Helper.getServletPath();
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
            <p>Sign the following text in the text area by using one of your U-Prove token.</p>
            <form class="form-horizontal" action="<%= url %>/WalletsServlet" method="get">
                <textarea name="document" class="form-control" cols="55" rows="10">Lorem ipsum dolor sit amet, consectetur adipiscing elit. Duis aliquam turpis eget nunc placerat laoreet. Aliquam semper molestie accumsan. Sed ultrices non urna nec rutrum.</textarea>
                <br />

                <div style="float: left;">
                    <input type="checkbox" disabled checked> Name
                    <input type="checkbox" name="attribute_2" value="1"> Age
                    <input type="checkbox" name="attribute_3" value="1"> Citizenship

                    <input type="hidden" name="attribute_1" value="1">
                </div>

                <input type="hidden" name="message" value="present_token">
                <input type="hidden" name="service_provider" value="sign-doc">

                <div style="float: right;">
                    <button type="submit" class="btn btn-primary btn-sm">Sign document</button>
                </div>
            </form>
        </div>
        <div class="col-lg-4">
            <h3>Verifier what?</h3>

            <p>A service provider will check if your token is valid and then receive the passed attributes.</p>
            <p>By using one of your tokens you only tell what is required and nothing more.</p>
        </div>
    </div>
</div>

<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
<script src="https://code.jquery.com/jquery.js"></script>
<!-- Include all compiled plugins (below), or include individual files as needed -->
<script src="../../js/bootstrap.min.js"></script>
</body>
</html>