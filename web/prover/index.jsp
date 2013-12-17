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
    <title>Prover</title>
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
            <a class="navbar-brand" href="#">Prover</a>
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
                        <li><a href="<%= url %>/service-provider/danish-tax">Document signing (Verifier)</a></li>
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
            <h1>Hello</h1>
            <p>This service let you generate tokens, which you can use to authenticate yourself at a service provider.</p>
            <p>Use the button on the right, to get your personal data from wayf (simulation).</p>
            <p>When you have chosen how many tokens you want to generate (each token can only be used once) and clicked "Generate tokens", are you ready to login at a service provider by using one of your tokens.</p>
            <p>By doing this you are sure that the service provider only know least possible about yourself.</p>
        </div>
        <div class="col-lg-4">
            <div class="well">
                <h3 style="margin-top: 10px;">wayf or?</h3>

                <p>Choose wayf to get your attributes.</p>

                <p>
                    <a href="../third-party/" class="btn btn-primary btn-success btn-sm" role="button">Use
                        wayf</a>
                </p>
            </div>
        </div>
    </div>
</div>

<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
<script src="https://code.jquery.com/jquery.js"></script>
<!-- Include all compiled plugins (below), or include individual files as needed -->
<script src="../js/bootstrap.min.js"></script>
</body>
</html>