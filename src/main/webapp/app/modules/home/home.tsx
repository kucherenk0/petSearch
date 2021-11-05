import React from 'react';
import { Link } from 'react-router-dom';
import { Translate } from 'react-jhipster';
import { Alert, Col, Row } from 'reactstrap';
import { useAppSelector } from 'app/config/store';
import './home.scss';
import SearchForm from 'app/components/SearchForm/SearchForm';
import PhotoForm from 'app/components/PhotoForm/PhotoForm';

export const Home = () => {
	const account = useAppSelector(state => state.authentication.account);

	return (
		<Row style={{ justifyContent: 'center', height: '100%' }}>
			<Col md="10" style={{ marginTop: 50 }}>
				<h1>
					{/* Загляни в папку i18n (home.title  - это путь к тексту)*/}
					<Translate contentKey="home.title" />
				</h1>
				<p className="lead">
					<Translate contentKey="home.subtitle">
						This is your homepage
					</Translate>
				</p>
				{
					// TODO
					account && account.login ? (
						<>
							<SearchForm />
							<br />
							<PhotoForm />
						</>
					) : (
						<div>
							<Alert color="warning">
								<Translate contentKey="global.messages.info.authenticated.prefix">
									If you want to{' '}
								</Translate>

								<Link to="/login" className="alert-link">
									<Translate contentKey="global.messages.info.authenticated.link">
										{' '}
										sign in
									</Translate>
								</Link>
								<Translate contentKey="global.messages.info.authenticated.suffix">
									, you can try the default accounts:
									<br />- Administrator (login=&quot;admin&quot; and
									password=&quot;admin&quot;)
									<br />- User (login=&quot;user&quot; and
									password=&quot;user&quot;).
								</Translate>
							</Alert>

							<Alert color="warning">
								<Translate contentKey="global.messages.info.register.noaccount">
									You do not have an account yet?
								</Translate>
								&nbsp;
								<Link to="/account/register" className="alert-link">
									<Translate contentKey="global.messages.info.register.link">
										Register a new account
									</Translate>
								</Link>
							</Alert>
						</div>
					)
				}
			</Col>
		</Row>
	);
};

export default Home;
