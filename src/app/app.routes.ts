import { Routes } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { ListsComponent } from './lists/lists.component';
import { LoginComponent } from './login/login.component';
import { PageNotFoundComponent } from './page-not-found/page-not-found.component';

export const routes: Routes = [
    {
        path: 'login',
        title: 'Login Page',
        component: LoginComponent
    },
    {
        path: 'home',
        title: 'Home Page',
        component: HomeComponent
    },
    {
        path: 'lists',
        title: 'Lists Page',
        component: ListsComponent
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
