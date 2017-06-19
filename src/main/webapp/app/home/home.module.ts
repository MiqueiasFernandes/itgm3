import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ItgmSharedModule } from '../shared';

import { HOME_ROUTE, HomeComponent } from './';

import { HomeService } from './home.service';

@NgModule({
    imports: [
        ItgmSharedModule,
        RouterModule.forRoot([ HOME_ROUTE ], { useHash: true })
    ],
    declarations: [
        HomeComponent,
    ],
    entryComponents: [
    ],
    providers: [
        HomeService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ItgmHomeModule {}
