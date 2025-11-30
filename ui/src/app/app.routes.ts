import { Routes } from '@angular/router';
import { HomeComponent } from './features/home/home.component';
import { LoginComponent } from './features/auth/login/login.component';
import { PageNotFoundComponent } from './core/page-not-found/page-not-found.component';
import { RegisterComponent } from './features/auth/register/register.component';
import { UserListComponent } from './features/lists/user-list/user-list.component';
import { GroupsComponent } from './features/groups/groups/groups.component';

export const routes: Routes = [
    {
        path: 'login',
        title: 'Login Page',
        component: LoginComponent
    },
    {
        path: 'login/register',
        title: 'Register Page',
        component: RegisterComponent
    },
    {
        path: 'home',
        title: 'Home Page',
        component: HomeComponent
    },
    {
        path: 'lists/user-list',
        title: 'My List',
        component: UserListComponent
    },
        {
        path: 'groups',
        title: 'My Groups',
        component: GroupsComponent
    },
    {
        path: '',
        redirectTo: '/login',
        pathMatch: 'full'
    },
    {
        path: '**',
        component: PageNotFoundComponent
    }
];
