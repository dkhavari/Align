<%@ include file="templates/header.jsp"%>

<%@ include file="templates/navbar_min.jsp"%>

<body style="background-image: url('images/retina_wood.png')">

	<!-- Standard log-in form. -->
	<div id="loginbox">
		<div class="large-content">
			<div class="row">
				<div class="col-lg-4 col-lg-offset-4">
					<div class="text-center feature">
						<a href="#" onclick="hide_login();show_create()"></a>
						<h2 class="padding alazarin" style="margin-bottom: 0px">New User?</h2>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-lg-4 col-lg-offset-4">
					<div class="clearfix padding white">
						<form id="login" action="Login" method="get">
							<input class="form-control input-lg styled-input margin-bottom"
								type="text" name="username" placeholder="Username"> <input
								class="form-control input-lg styled-input" type="password"
								name="password" placeholder="Password">
						</form>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-lg-4 col-lg-offset-4">
					<div class="text-center feature">
						<a href="#" onclick="document.getElementById('login').submit()"></a>
						<h2 class="padding emerald" style="margin-top: 0px">Log In</h2>
					</div>
				</div>
			</div>
		</div>
	</div>

	<!-- Create new user form. -->
	<div id="createbox" style="display: none">
		<div class="large-content" style="height: 505px;">
			<div class="row">
				<div class="col-lg-4 col-lg-offset-4">
					<div class="text-center feature">
						<a href="#" onclick="hide_create(); show_login()"></a>
						<h2 class="padding river" style="margin-bottom: 0px">Returning?</h2>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-lg-4 col-lg-offset-4">
					<div class="clearfix padding white">
						<form id="new" action="CreateUser" method="post">
							<input class="form-control input-lg styled-input margin-bottom"
								type="text" name="realname" placeholder="Real Name"> <input
								class="form-control input-lg styled-input margin-bottom"
								type="text" name="username" placeholder="Username"> <input
								class="form-control input-lg styled-input" type="password"
								name="password" placeholder="Password">
						</form>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-lg-4 col-lg-offset-4">
					<div class="text-center feature">
						<button type="submit" style="display: none"></button>
						<a href="#" onclick="document.getElementById('new').submit()"></a>
						<h2 class="padding emerald" style="margin-top: 0px">Create + Enter</h2>
					</div>
				</div>
			</div>
		</div>
	</div>


</body>

<!-- Scripts for show/hide of login boxes. -->
<script type="text/javascript">
	function hide_login() {
		var login = document.getElementById('loginbox');
		login.style.display = 'none';
	}
	function show_login() {
		var login = document.getElementById('loginbox');
		login.style.display = 'block';
	}
	function show_create() {
		var create = document.getElementById('createbox');
		create.style.display = 'block';
	}
	function hide_create() {
		var create = document.getElementById('createbox');
		create.style.display = 'none';
	}
</script>

</html>