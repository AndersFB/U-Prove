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

    int numberOfTokens = WalletsServlet.getNumberOfTokens();
    String message = "You have " + numberOfTokens + " token(s) left.";
    if (numberOfTokens == 0)
        message += " <a href='../prover/'>Generate new wallets.</a>";

    String token = WalletsServlet.getToken();
    if (token == null)
        token = "";
%>
<html>
<head>
    <title>Token wallets</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <!-- Bootstrap -->
    <link href="../css/bootstrap.min.yeti.css" rel="stylesheet">
    <link href="../css/custom.yeti.css" rel="stylesheet">

    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
    <script src="https://oss.maxcdn.com/libs/respond.js/1.3.0/respond.min.js"></script>
    <![endif]-->
</head>
<body>
<nav class="navbar navbar-default navbar-fixed-top" role="navigation">
    <div class="container">
        <!-- Brand and toggle get grouped for better mobile display -->
        <div class="navbar-header">
            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#navbar-collapse-1">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="#">Token wallets</a>
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
            <h1>Your token wallets</h1>

            <p><%= message %>
            </p>

            <p>Now try to use one of your token at one of the service provider.</p>
            <ul>
                <li><a href="../service-provider/car-rental">Car rental (min 18 years old)</a></li>
                <li><a href="../service-provider/elections">Voting for elections to the danish national parliament (min
                    18 years old and danish citizenship)</a></li>
                <li><a href="../service-provider/danish-tax">The danish tax authority (danish citizenship)</a></li>
                <li><a href="../service-provider/sign-doc">Document signing (your name)</a></li>
            </ul>

            <div class="clearfix"></div>

            <form class="form-horizontal" action="<%= url %>/WalletsServlet" method="get">
            <div style="float: left;">
                <h3>Generate a token</h3>
            </div>

                <div style="float: right; margin-top: 14px;">
                <button type="submit" class="btn btn-primary btn-sm">Get token</button>
                    </div>
                <input type="hidden" name="message" value="get_token">
            </form>

            <textarea class="form-control" cols="55" rows="20"><%= token %></textarea>
            <br/>
        </div>
        <div class="col-lg-4">
            <h3>What is this?</h3>

            <p>This is your token wallets where you can keep track of how many tokens you have left.</p>

            <p>It is possible to simulate that a token is stored to your hard drive or a USB by clicking "Get token".</p>

            <p>Copy the content in the text area and save it in a file.</p>

            <p>At the service provider it is possible to simulating that the token is fetched from your hard drive or
                USB. At that point you need the generated token.</p>

            <p>It is not possible to regenerate the token in the text area, only to generate a new token.</p>
        </div>
    </div>
</div>

<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
<script src="https://code.jquery.com/jquery.js"></script>
<!-- Include all compiled plugins (below), or include individual files as needed -->
<script src="../js/bootstrap.min.js"></script>
</body>
</html>