import React from 'react';
import { Translate } from 'react-jhipster';

import { NavItem, NavLink, NavbarBrand } from 'reactstrap';
import { NavLink as Link } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

export const BrandIcon = props => (
	<div {...props} className="brand-icon">
		<img src="content/images/dogg.svg" alt="Logo" />
	</div>
);

export const Brand = () => (
	<NavbarBrand tag={Link} to="/" className="brand-logo">
		<BrandIcon />
		<span className="brand-title">Petsearch</span>
	</NavbarBrand>
);

export const Home = () => (
	<NavItem>
		<NavLink tag={Link} to="/" className="d-flex align-items-center">
			<FontAwesomeIcon icon="home" />
			<span>Домашняя страница</span>
		</NavLink>
	</NavItem>
);
