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
    <title>Third party such as wayf or?</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <!-- Bootstrap -->
    <link href="../css/bootstrap.min.flatly.css" rel="stylesheet">
    <link href="../css/custom.flatly.css" rel="stylesheet">

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
            <a class="navbar-brand" href="#">Third party such as wayf or?</a>
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
            <div class="well">
                <form class="form-horizontal" action="../prover/generate-tokens" method="get">
                    <fieldset>
                        <legend>Your information</legend>
                        <div class="form-group">
                            <label for="inputName" class="col-lg-2 control-label">Name</label>
                            <div class="col-lg-10">
                                <input type="text" name="attribute_1" class="form-control" id="inputName" placeholder="(ex. Anders)">
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="inputAge" class="col-lg-2 control-label">Age</label>
                            <div class="col-lg-10">
                                <input type="text" name="attribute_2" class="form-control" id="inputAge" placeholder="(ex. 23)">
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="inputCitizenship" class="col-lg-2 control-label">Citizenship</label>
                            <div class="col-lg-10">
                                <input type="text" name="attribute_3" class="form-control" id="inputCitizenship" placeholder="(ex. Danish)">
                            </div>
                        </div>
                        <input type="hidden" name="numberOfAttributes" value="3"/>
                        <div class="form-group">
                            <div class="col-lg-10 col-lg-offset-2">
                                <button type="submit" class="btn btn-primary btn-success">Submit information</button>
                            </div>
                        </div>
                    </fieldset>
                </form>
            </div>
        </div>
        <div class="col-lg-4">
            <h3>In the real world...</h3>

            <p>In a real world implementation the information is catched from a trusted service such as wayf etc.</p>
        </div>
    </div>
</div>

<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
<script src="https://code.jquery.com/jquery.js"></script>
<!-- Include all compiled plugins (below), or include individual files as needed -->
<script src="../js/bootstrap.min.js"></script>
</body>
</html>