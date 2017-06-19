import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { PaginationUtil } from 'ng-jhipster';

import { ProjetoComponent } from './projeto.component';
import { ProjetoDetailComponent } from './projeto-detail.component';
import { ProjetoPopupComponent } from './projeto-dialog.component';
import { ProjetoDeletePopupComponent } from './projeto-delete-dialog.component';

import { Principal } from '../../shared';

@Injectable()
export class ProjetoResolvePagingParams implements Resolve<any> {

  constructor(private paginationUtil: PaginationUtil) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
      const page = route.queryParams['page'] ? route.queryParams['page'] : '1';
      const sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'id,asc';
      return {
          page: this.paginationUtil.parsePage(page),
          predicate: this.paginationUtil.parsePredicate(sort),
          ascending: this.paginationUtil.parseAscending(sort)
    };
  }
}

export const projetoRoute: Routes = [
  {
    path: 'projeto',
    component: ProjetoComponent,
    resolve: {
      'pagingParams': ProjetoResolvePagingParams
    },
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'itgmApp.projeto.home.title'
    },
    canActivate: [UserRouteAccessService]
  }, {
    path: 'projeto/:id',
    component: ProjetoDetailComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'itgmApp.projeto.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const projetoPopupRoute: Routes = [
  {
    path: 'projeto-new',
    component: ProjetoPopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'itgmApp.projeto.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  },
  {
    path: 'projeto/:id/edit',
    component: ProjetoPopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'itgmApp.projeto.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  },
  {
    path: 'projeto/:id/delete',
    component: ProjetoDeletePopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'itgmApp.projeto.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
