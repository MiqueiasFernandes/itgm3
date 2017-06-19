import './vendor.ts';

import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { Ng2Webstorage } from 'ng2-webstorage';

import { ItgmSharedModule, UserRouteAccessService } from './shared';
import { ItgmHomeModule } from './home/home.module';
import { ItgmAdminModule } from './admin/admin.module';
import { ItgmAccountModule } from './account/account.module';
import { ItgmEntityModule } from './entities/entity.module';

import { LayoutRoutingModule } from './layouts';
import { customHttpProvider } from './blocks/interceptor/http.provider';
import { PaginationConfig } from './blocks/config/uib-pagination.config';


import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import {
    JhiMainComponent,
    NavbarComponent,
    FooterComponent,
    ProfileService,
    PageRibbonComponent,
    ActiveMenuDirective,
    ErrorComponent,
    ShareComponent,
    ShareService,
    FabAddComponent,
    SidebarComponent,
    SidebarService,
    FolderComponent
} from './layouts';

@NgModule({
    imports: [
        BrowserModule,
        BrowserAnimationsModule,
        LayoutRoutingModule,
        Ng2Webstorage.forRoot({ prefix: 'jhi', separator: '-'}),
        ItgmSharedModule,
        ItgmHomeModule,
        ItgmAdminModule,
        ItgmAccountModule,
        ItgmEntityModule
    ],
    declarations: [
        JhiMainComponent,
        NavbarComponent,
        ErrorComponent,
        PageRibbonComponent,
        ActiveMenuDirective,
        FooterComponent,
        FabAddComponent,
        ShareComponent,
        FabAddComponent,
        SidebarComponent,
        FolderComponent
    ],
    entryComponents: [
        ShareComponent,
        FolderComponent
    ],
    providers: [
        ProfileService,
        customHttpProvider(),
        PaginationConfig,
        UserRouteAccessService,
        SidebarService,
        ShareService,
    ],
    bootstrap: [ JhiMainComponent ]
})
export class ItgmAppModule {}
